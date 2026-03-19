package com.Man10h.social_network_app.clean_architecture.infrastructure.security;

import com.Man10h.social_network_app.clean_architecture.application.port.TokenIssuer;
import com.Man10h.social_network_app.clean_architecture.domain.shared.exception.ResourceNotFoundException;
import com.Man10h.social_network_app.clean_architecture.domain.user.entity.User;
import com.Man10h.social_network_app.model.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class LegacyTokenIssuerAdapter implements TokenIssuer {
    private final com.Man10h.social_network_app.service.TokenService tokenService;
    private final com.Man10h.social_network_app.repository.UserRepository userRepository;

    public LegacyTokenIssuerAdapter(
            com.Man10h.social_network_app.service.TokenService tokenService,
            com.Man10h.social_network_app.repository.UserRepository userRepository
    ) {
        this.tokenService = tokenService;
        this.userRepository = userRepository;
    }

    @Override
    public String issue(User user) {
        UserEntity userEntity = resolveUser(user);
        return tokenService.generateToken(userEntity);
    }

    private UserEntity resolveUser(User user) {
        if (user.id() != null) {
            return userRepository.findById(user.id())
                    .orElseThrow(() -> new ResourceNotFoundException("User not found"));
        }
        return userRepository.findByUsername(user.username())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
