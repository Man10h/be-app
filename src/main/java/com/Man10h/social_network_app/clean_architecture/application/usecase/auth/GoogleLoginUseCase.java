package com.Man10h.social_network_app.clean_architecture.application.usecase.auth;

import com.Man10h.social_network_app.clean_architecture.application.port.GoogleIdentityProfile;
import com.Man10h.social_network_app.clean_architecture.application.port.GoogleIdentityVerifier;
import com.Man10h.social_network_app.clean_architecture.application.port.TokenIssuer;
import com.Man10h.social_network_app.clean_architecture.domain.shared.exception.UnauthorizedActionException;
import com.Man10h.social_network_app.clean_architecture.domain.shared.exception.ValidationException;
import com.Man10h.social_network_app.clean_architecture.domain.user.entity.User;
import com.Man10h.social_network_app.clean_architecture.domain.user.entity.UserRole;
import com.Man10h.social_network_app.clean_architecture.domain.user.repository.UserAccountRepository;
import com.Man10h.social_network_app.clean_architecture.domain.user.repository.UserRoleRepository;

public class GoogleLoginUseCase {
    private final UserAccountRepository userAccountRepository;
    private final UserRoleRepository userRoleRepository;
    private final GoogleIdentityVerifier googleIdentityVerifier;
    private final TokenIssuer tokenIssuer;

    public GoogleLoginUseCase(
            UserAccountRepository userAccountRepository,
            UserRoleRepository userRoleRepository,
            GoogleIdentityVerifier googleIdentityVerifier,
            TokenIssuer tokenIssuer
    ) {
        this.userAccountRepository = userAccountRepository;
        this.userRoleRepository = userRoleRepository;
        this.googleIdentityVerifier = googleIdentityVerifier;
        this.tokenIssuer = tokenIssuer;
    }

    public String execute(String idToken) {
        if (idToken == null || idToken.isBlank()) {
            throw new ValidationException("Google id token is required");
        }

        GoogleIdentityProfile profile = googleIdentityVerifier.verify(idToken);
        User user = userAccountRepository.findByEmail(profile.email())
                .orElseGet(() -> createUser(profile));

        if (!user.enabled()) {
            throw new UnauthorizedActionException("User is disabled");
        }

        return tokenIssuer.issue(user);
    }

    private User createUser(GoogleIdentityProfile profile) {
        UserRole role = userRoleRepository.findDefaultRole()
                .orElseThrow(() -> new ValidationException("Default user role is missing"));

        String displayName = profile.displayName() == null || profile.displayName().isBlank()
                ? profile.email()
                : profile.displayName().trim();

        return userAccountRepository.save(
                User.registerExternal(
                        profile.email(),
                        profile.email(),
                        displayName,
                        displayName,
                        "F",
                        role
                )
        );
    }
}
