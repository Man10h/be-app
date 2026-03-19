package com.Man10h.social_network_app.clean_architecture.infrastructure.media;

import com.Man10h.social_network_app.clean_architecture.application.port.UserProfileImageStorage;
import com.Man10h.social_network_app.clean_architecture.application.shared.UploadedFile;
import com.Man10h.social_network_app.clean_architecture.domain.shared.exception.ResourceNotFoundException;
import com.Man10h.social_network_app.model.entity.UserEntity;
import org.springframework.stereotype.Component;

@Component
public class LegacyUserProfileImageStorageAdapter implements UserProfileImageStorage {
    private final com.Man10h.social_network_app.repository.UserRepository userRepository;
    private final com.Man10h.social_network_app.service.ImageService imageService;

    public LegacyUserProfileImageStorageAdapter(
            com.Man10h.social_network_app.repository.UserRepository userRepository,
            com.Man10h.social_network_app.service.ImageService imageService
    ) {
        this.userRepository = userRepository;
        this.imageService = imageService;
    }

    @Override
    public void replace(String userId, UploadedFile image) {
        UserEntity userEntity = userRepository.findById(userId)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        imageService.deleteByUserEntity(userEntity);
        imageService.createImageWithUserEntity(toMultipartFile(image), userEntity);
    }

    private ByteArrayMultipartFile toMultipartFile(UploadedFile image) {
        return new ByteArrayMultipartFile(image.originalFilename(), image.contentType(), image.content());
    }
}
