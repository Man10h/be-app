package com.Man10h.social_network_app.clean_architecture.domain.user.entity;

public record UserRole(Long id, String name) {
    public UserRole {
        if (id == null) {
            throw new IllegalArgumentException("Role id is required");
        }
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Role name is required");
        }
    }
}
