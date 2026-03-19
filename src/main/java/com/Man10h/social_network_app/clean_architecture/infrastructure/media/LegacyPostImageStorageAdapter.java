package com.Man10h.social_network_app.clean_architecture.infrastructure.media;

import com.Man10h.social_network_app.clean_architecture.application.port.PostImageStorage;
import com.Man10h.social_network_app.clean_architecture.application.shared.UploadedFile;
import com.Man10h.social_network_app.clean_architecture.domain.shared.exception.ResourceNotFoundException;
import com.Man10h.social_network_app.model.entity.PostEntity;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LegacyPostImageStorageAdapter implements PostImageStorage {
    private final com.Man10h.social_network_app.repository.PostRepository postRepository;
    private final com.Man10h.social_network_app.service.ImageService imageService;

    public LegacyPostImageStorageAdapter(
            com.Man10h.social_network_app.repository.PostRepository postRepository,
            com.Man10h.social_network_app.service.ImageService imageService
    ) {
        this.postRepository = postRepository;
        this.imageService = imageService;
    }

    @Override
    public void attach(String postId, List<UploadedFile> images) {
        PostEntity postEntity = loadPost(postId);
        for (UploadedFile image : images) {
            if (image != null && !image.isEmpty()) {
                imageService.createImageWithPostEntity(toMultipartFile(image), postEntity);
            }
        }
    }

    @Override
    public void deleteAll(String postId) {
        imageService.deleteByPostEntity(loadPost(postId));
    }

    private PostEntity loadPost(String postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
    }

    private ByteArrayMultipartFile toMultipartFile(UploadedFile image) {
        return new ByteArrayMultipartFile(image.originalFilename(), image.contentType(), image.content());
    }
}
