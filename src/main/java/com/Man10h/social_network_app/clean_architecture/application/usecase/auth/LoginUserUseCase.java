package com.Man10h.social_network_app.clean_architecture.application.usecase.auth;

import com.Man10h.social_network_app.clean_architecture.application.port.CredentialsAuthenticator;
import com.Man10h.social_network_app.clean_architecture.application.port.PasswordHasher;
import com.Man10h.social_network_app.clean_architecture.application.port.TokenIssuer;
import com.Man10h.social_network_app.clean_architecture.domain.shared.exception.UnauthorizedActionException;
import com.Man10h.social_network_app.clean_architecture.domain.shared.exception.ValidationException;
import com.Man10h.social_network_app.clean_architecture.domain.user.entity.User;
import com.Man10h.social_network_app.clean_architecture.domain.user.repository.UserAccountRepository;

public class LoginUserUseCase {
    private final UserAccountRepository userAccountRepository;
    private final PasswordHasher passwordHasher;
    private final CredentialsAuthenticator credentialsAuthenticator;
    private final TokenIssuer tokenIssuer;

    public LoginUserUseCase(
            UserAccountRepository userAccountRepository,
            PasswordHasher passwordHasher,
            CredentialsAuthenticator credentialsAuthenticator,
            TokenIssuer tokenIssuer
    ) {
        this.userAccountRepository = userAccountRepository;
        this.passwordHasher = passwordHasher;
        this.credentialsAuthenticator = credentialsAuthenticator;
        this.tokenIssuer = tokenIssuer;
    }

    public String execute(LoginUserCommand command) {
        if (command == null) {
            throw new ValidationException("Login command is required");
        }

        User user = userAccountRepository.findByUsername(command.username())
                .orElseThrow(() -> new UnauthorizedActionException("Invalid username or password"));

        if (!user.canAuthenticate() || !passwordHasher.matches(command.password(), user.passwordHash())) {
            throw new UnauthorizedActionException("Invalid username or password");
        }

        credentialsAuthenticator.authenticate(command.username(), command.password());
        return tokenIssuer.issue(user);
    }
}
