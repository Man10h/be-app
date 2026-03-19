package com.Man10h.social_network_app.clean_architecture.domain.shared.exception;

public class ValidationException extends RuntimeException {
    public ValidationException(String message) {
        super(message);
    }
}
