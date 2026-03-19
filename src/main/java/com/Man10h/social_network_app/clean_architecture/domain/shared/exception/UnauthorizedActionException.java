package com.Man10h.social_network_app.clean_architecture.domain.shared.exception;

public class UnauthorizedActionException extends RuntimeException {
    public UnauthorizedActionException(String message) {
        super(message);
    }
}
