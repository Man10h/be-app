package com.Man10h.social_network_app.clean_architecture.application.port;

public interface GoogleIdentityVerifier {
    GoogleIdentityProfile verify(String idToken);
}
