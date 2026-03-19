package com.Man10h.social_network_app.clean_architecture.infrastructure.security;

import com.Man10h.social_network_app.clean_architecture.application.port.CredentialsAuthenticator;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SpringCredentialsAuthenticatorAdapter implements CredentialsAuthenticator {
    private final AuthenticationManager authenticationManager;

    public SpringCredentialsAuthenticatorAdapter(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @Override
    public void authenticate(String username, String password) {
        UsernamePasswordAuthenticationToken authentication =
                new UsernamePasswordAuthenticationToken(username, password);
        SecurityContextHolder.getContext().setAuthentication(authenticationManager.authenticate(authentication));
    }
}
