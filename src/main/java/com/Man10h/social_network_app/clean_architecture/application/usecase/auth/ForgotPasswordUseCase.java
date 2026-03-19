package com.Man10h.social_network_app.clean_architecture.application.usecase.auth;

import com.Man10h.social_network_app.clean_architecture.application.port.MailGateway;
import com.Man10h.social_network_app.clean_architecture.application.port.PasswordHasher;
import com.Man10h.social_network_app.clean_architecture.application.port.RandomCodeGenerator;
import com.Man10h.social_network_app.clean_architecture.domain.shared.exception.UnauthorizedActionException;
import com.Man10h.social_network_app.clean_architecture.domain.shared.exception.ValidationException;
import com.Man10h.social_network_app.clean_architecture.domain.user.entity.User;
import com.Man10h.social_network_app.clean_architecture.domain.user.repository.UserAccountRepository;

public class ForgotPasswordUseCase {
    private final UserAccountRepository userAccountRepository;
    private final PasswordHasher passwordHasher;
    private final RandomCodeGenerator randomCodeGenerator;
    private final MailGateway mailGateway;

    public ForgotPasswordUseCase(
            UserAccountRepository userAccountRepository,
            PasswordHasher passwordHasher,
            RandomCodeGenerator randomCodeGenerator,
            MailGateway mailGateway
    ) {
        this.userAccountRepository = userAccountRepository;
        this.passwordHasher = passwordHasher;
        this.randomCodeGenerator = randomCodeGenerator;
        this.mailGateway = mailGateway;
    }

    public void execute(String email) {
        if (email == null || email.isBlank()) {
            throw new ValidationException("Email is required");
        }

        User user = userAccountRepository.findByEmail(email.trim())
                .orElseThrow(() -> new ValidationException("Email is not registered"));

        if (!user.enabled()) {
            throw new UnauthorizedActionException("User is disabled");
        }

        String newPassword = randomCodeGenerator.generateNumericCode();
        User updatedUser = user.resetPassword(passwordHasher.hash(newPassword));
        userAccountRepository.save(updatedUser);
        mailGateway.sendHtml(
                updatedUser.email(),
                "New Password",
                "<html>New Password: " + newPassword + "</html>"
        );
    }
}
