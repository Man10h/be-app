package com.Man10h.social_network_app.exception;

import com.Man10h.social_network_app.exception.exceptions.GlobalException;
import com.Man10h.social_network_app.exception.exceptions.NotFoundException;
import com.Man10h.social_network_app.exception.exceptions.UnauthorizedException;
import com.Man10h.social_network_app.exception.exceptions.UniqueConstraintException;
import com.Man10h.social_network_app.model.dto.ErrorDTO;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class Handler {

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorDTO> notFound(NotFoundException e) {
        return ResponseEntity.ok(ErrorDTO.builder()
                        .message(e.getMessage())
                        .code(HttpStatus.NOT_FOUND.value())
                        .error(HttpStatus.NOT_FOUND.getReasonPhrase())
                        .build()
        );
    }

    @ExceptionHandler(UniqueConstraintException.class)
    public ResponseEntity<ErrorDTO> uniqueConstraint(UniqueConstraintException e) {
        return ResponseEntity.ok(ErrorDTO.builder()
                        .message(e.getMessage())
                        .code(HttpStatus.BAD_REQUEST.value())
                        .error(HttpStatus.BAD_REQUEST.getReasonPhrase())
                        .build()
        );
    }

    @ExceptionHandler(GlobalException.class)
    public ResponseEntity<ErrorDTO> global(GlobalException e) {
        return ResponseEntity.ok(
          ErrorDTO.builder()
                  .message(e.getMessage())
                  .build()
        );
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ErrorDTO> unauthorized(UnauthorizedException e) {
        return ResponseEntity.ok(ErrorDTO.builder()
                .message(e.getMessage())
                .code(HttpStatus.UNAUTHORIZED.value())
                .error(HttpStatus.UNAUTHORIZED.getReasonPhrase())
                .build()
        );
    }


}
