package com.Man10h.social_network_app.clean_architecture.application.usecase.auth;

import com.Man10h.social_network_app.clean_architecture.application.port.MailGateway;
import com.Man10h.social_network_app.clean_architecture.application.port.PasswordHasher;
import com.Man10h.social_network_app.clean_architecture.domain.shared.exception.ConflictException;
import com.Man10h.social_network_app.clean_architecture.domain.shared.exception.ValidationException;
import com.Man10h.social_network_app.clean_architecture.domain.user.entity.User;
import com.Man10h.social_network_app.clean_architecture.domain.user.entity.UserRole;
import com.Man10h.social_network_app.clean_architecture.domain.user.repository.UserAccountRepository;
import com.Man10h.social_network_app.clean_architecture.domain.user.repository.UserRoleRepository;

public class RegisterUserUseCase {
    private final UserAccountRepository userAccountRepository;
    private final UserRoleRepository userRoleRepository;
    private final PasswordHasher passwordHasher;
    private final MailGateway mailGateway;

    public RegisterUserUseCase(
            UserAccountRepository userAccountRepository,
            UserRoleRepository userRoleRepository,
            PasswordHasher passwordHasher,
            MailGateway mailGateway
    ) {
        this.userAccountRepository = userAccountRepository;
        this.userRoleRepository = userRoleRepository;
        this.passwordHasher = passwordHasher;
        this.mailGateway = mailGateway;
    }

    public User execute(RegisterUserCommand command) {
        if (command == null) {
            throw new ValidationException("Register command is required");
        }
        if (userAccountRepository.findByUsername(command.username()).isPresent()) {
            throw new ConflictException("Username already exists");
        }
        if (userAccountRepository.findByEmail(command.email()).isPresent()) {
            throw new ConflictException("Email already exists");
        }

        UserRole role = userRoleRepository.findDefaultRole()
                .orElseThrow(() -> new ValidationException("Default user role is missing"));

        User user = User.registerNew(
                command.username(),
                command.email(),
                passwordHasher.hash(command.password()),
                command.firstName(),
                command.lastName(),
                command.gender(),
                role
        );

        User savedUser = userAccountRepository.save(user);
        mailGateway.sendHtml(
                savedUser.email(),
                "Welcome!",
                "<html>Welcome, " + savedUser.username() + "!</html>"
        );
        return savedUser;
    }
}
