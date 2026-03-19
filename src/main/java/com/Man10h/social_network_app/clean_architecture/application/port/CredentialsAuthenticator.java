package com.Man10h.social_network_app.clean_architecture.application.port;

public interface CredentialsAuthenticator {
    void authenticate(String username, String password);
}
