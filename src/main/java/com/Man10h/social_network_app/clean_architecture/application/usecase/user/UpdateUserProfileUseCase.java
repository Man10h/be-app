package com.Man10h.social_network_app.clean_architecture.application.usecase.user;

import com.Man10h.social_network_app.clean_architecture.application.port.UserProfileImageStorage;
import com.Man10h.social_network_app.clean_architecture.domain.shared.exception.ResourceNotFoundException;
import com.Man10h.social_network_app.clean_architecture.domain.shared.exception.ValidationException;
import com.Man10h.social_network_app.clean_architecture.domain.user.entity.User;
import com.Man10h.social_network_app.clean_architecture.domain.user.repository.UserAccountRepository;

public class UpdateUserProfileUseCase {
    private final UserAccountRepository userAccountRepository;
    private final UserProfileImageStorage userProfileImageStorage;

    public UpdateUserProfileUseCase(
            UserAccountRepository userAccountRepository,
            UserProfileImageStorage userProfileImageStorage
    ) {
        this.userAccountRepository = userAccountRepository;
        this.userProfileImageStorage = userProfileImageStorage;
    }

    public User execute(UpdateUserProfileCommand command) {
        if (command == null) {
            throw new ValidationException("Update profile command is required");
        }

        User user = userAccountRepository.findById(command.userId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        User updatedUser = user.updateProfile(command.firstName(), command.lastName(), command.gender());
        User savedUser = userAccountRepository.save(updatedUser);

        if (command.image() != null && !command.image().isEmpty()) {
            userProfileImageStorage.replace(savedUser.id(), command.image());
        }

        return userAccountRepository.findProfileById(savedUser.id()).orElse(savedUser);
    }
}
