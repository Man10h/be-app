package com.Man10h.social_network_app.clean_architecture.interfaces.rest;

import com.Man10h.social_network_app.clean_architecture.domain.shared.exception.ConflictException;
import com.Man10h.social_network_app.clean_architecture.domain.shared.exception.ResourceNotFoundException;
import com.Man10h.social_network_app.clean_architecture.domain.shared.exception.UnauthorizedActionException;
import com.Man10h.social_network_app.clean_architecture.domain.shared.exception.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice(assignableTypes = {
        CleanAuthController.class,
        CleanUserController.class,
        CleanPostController.class
})
public class CleanArchitectureExceptionHandler {
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiError> handleNotFound(ResourceNotFoundException exception) {
        return error(HttpStatus.NOT_FOUND, exception.getMessage());
    }

    @ExceptionHandler(ConflictException.class)
    public ResponseEntity<ApiError> handleConflict(ConflictException exception) {
        return error(HttpStatus.CONFLICT, exception.getMessage());
    }

    @ExceptionHandler(UnauthorizedActionException.class)
    public ResponseEntity<ApiError> handleUnauthorized(UnauthorizedActionException exception) {
        return error(HttpStatus.UNAUTHORIZED, exception.getMessage());
    }

    @ExceptionHandler({ValidationException.class, IllegalArgumentException.class})
    public ResponseEntity<ApiError> handleBadRequest(RuntimeException exception) {
        return error(HttpStatus.BAD_REQUEST, exception.getMessage());
    }

    private ResponseEntity<ApiError> error(HttpStatus status, String message) {
        return ResponseEntity.status(status)
                .body(new ApiError(message, status.value(), status.getReasonPhrase()));
    }

    public record ApiError(String message, int code, String error) {
    }
}
