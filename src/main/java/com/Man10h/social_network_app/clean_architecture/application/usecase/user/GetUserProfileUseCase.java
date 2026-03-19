package com.Man10h.social_network_app.clean_architecture.application.usecase.user;

import com.Man10h.social_network_app.clean_architecture.domain.shared.exception.ResourceNotFoundException;
import com.Man10h.social_network_app.clean_architecture.domain.user.entity.User;
import com.Man10h.social_network_app.clean_architecture.domain.user.repository.UserAccountRepository;

public class GetUserProfileUseCase {
    private final UserAccountRepository userAccountRepository;

    public GetUserProfileUseCase(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    public User execute(String userId) {
        return userAccountRepository.findProfileById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
