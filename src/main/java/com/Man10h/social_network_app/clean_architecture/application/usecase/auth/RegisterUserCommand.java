package com.Man10h.social_network_app.clean_architecture.application.usecase.auth;

public record RegisterUserCommand(
        String username,
        String password,
        String email,
        String firstName,
        String lastName,
        String gender
) {
}
