package com.Man10h.social_network_app.exception;

import com.Man10h.social_network_app.exception.exceptions.NotFoundException;
import com.Man10h.social_network_app.model.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class Handler {

    @ExceptionHandler(NotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ErrorDTO notFound(NotFoundException e) {
        return ErrorDTO.builder()
                .message(e.getMessage())
                .code(400)
                .build();
    }
}
