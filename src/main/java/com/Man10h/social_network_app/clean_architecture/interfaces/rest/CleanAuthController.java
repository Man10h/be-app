package com.Man10h.social_network_app.clean_architecture.interfaces.rest;

import com.Man10h.social_network_app.clean_architecture.application.usecase.auth.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v2/clean/home")
public class CleanAuthController {
    private final RegisterUserUseCase registerUserUseCase;
    private final LoginUserUseCase loginUserUseCase;
    private final GoogleLoginUseCase googleLoginUseCase;
    private final ForgotPasswordUseCase forgotPasswordUseCase;

    public CleanAuthController(
            RegisterUserUseCase registerUserUseCase,
            LoginUserUseCase loginUserUseCase,
            GoogleLoginUseCase googleLoginUseCase,
            ForgotPasswordUseCase forgotPasswordUseCase
    ) {
        this.registerUserUseCase = registerUserUseCase;
        this.loginUserUseCase = loginUserUseCase;
        this.googleLoginUseCase = googleLoginUseCase;
        this.forgotPasswordUseCase = forgotPasswordUseCase;
    }

    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody RegisterRequest request) {
        registerUserUseCase.execute(
                new RegisterUserCommand(
                        request.username(),
                        request.password(),
                        request.email(),
                        request.firstName(),
                        request.lastName(),
                        request.gender()
                )
        );
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PostMapping("/login")
    public TokenResponse login(@RequestBody LoginRequest request) {
        String token = loginUserUseCase.execute(new LoginUserCommand(request.username(), request.password()));
        return new TokenResponse(token);
    }

    @PostMapping("/google")
    public TokenResponse googleLogin(@RequestBody GoogleLoginRequest request) {
        return new TokenResponse(googleLoginUseCase.execute(request.idToken()));
    }

    @PostMapping("/forgot-password")
    public ResponseEntity<Void> forgotPassword(@RequestBody ForgotPasswordRequest request) {
        forgotPasswordUseCase.execute(request.email());
        return ResponseEntity.ok().build();
    }

    public record RegisterRequest(
            String username,
            String password,
            String email,
            String firstName,
            String lastName,
            String gender
    ) {
    }

    public record LoginRequest(String username, String password) {
    }

    public record GoogleLoginRequest(String idToken) {
    }

    public record ForgotPasswordRequest(String email) {
    }

    public record TokenResponse(String token) {
    }
}
