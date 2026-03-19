package com.Man10h.social_network_app.clean_architecture.infrastructure.identity;

import com.Man10h.social_network_app.clean_architecture.application.port.GoogleIdentityProfile;
import com.Man10h.social_network_app.clean_architecture.application.port.GoogleIdentityVerifier;
import com.google.api.client.googleapis.auth.oauth2.GoogleIdToken;
import org.springframework.stereotype.Component;

@Component
public class LegacyGoogleIdentityVerifierAdapter implements GoogleIdentityVerifier {
    private final com.Man10h.social_network_app.service.TokenService tokenService;

    public LegacyGoogleIdentityVerifierAdapter(com.Man10h.social_network_app.service.TokenService tokenService) {
        this.tokenService = tokenService;
    }

    @Override
    public GoogleIdentityProfile verify(String idToken) {
        GoogleIdToken.Payload payload = tokenService.verifyIdToken(idToken);
        Object name = payload.get("name");
        String displayName = name == null ? payload.getSubject() : name.toString();
        return new GoogleIdentityProfile(payload.getEmail(), displayName);
    }
}
