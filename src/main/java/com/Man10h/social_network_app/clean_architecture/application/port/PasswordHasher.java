package com.Man10h.social_network_app.clean_architecture.application.port;

public interface PasswordHasher {
    String hash(String rawPassword);

    boolean matches(String rawPassword, String hashedPassword);
}
