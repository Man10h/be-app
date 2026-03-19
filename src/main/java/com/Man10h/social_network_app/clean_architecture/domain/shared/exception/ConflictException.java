package com.Man10h.social_network_app.clean_architecture.domain.shared.exception;

public class ConflictException extends RuntimeException {
    public ConflictException(String message) {
        super(message);
    }
}
