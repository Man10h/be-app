package com.Man10h.social_network_app.clean_architecture.application.usecase.post;

import com.Man10h.social_network_app.clean_architecture.application.port.PostImageStorage;
import com.Man10h.social_network_app.clean_architecture.application.shared.UploadedFile;
import com.Man10h.social_network_app.clean_architecture.domain.post.entity.Post;
import com.Man10h.social_network_app.clean_architecture.domain.post.repository.PostRepository;
import com.Man10h.social_network_app.clean_architecture.domain.shared.exception.ResourceNotFoundException;
import com.Man10h.social_network_app.clean_architecture.domain.shared.exception.ValidationException;
import com.Man10h.social_network_app.clean_architecture.domain.user.repository.UserAccountRepository;

import java.time.Instant;
import java.util.List;

public class CreatePostUseCase {
    private final UserAccountRepository userAccountRepository;
    private final PostRepository postRepository;
    private final PostImageStorage postImageStorage;

    public CreatePostUseCase(
            UserAccountRepository userAccountRepository,
            PostRepository postRepository,
            PostImageStorage postImageStorage
    ) {
        this.userAccountRepository = userAccountRepository;
        this.postRepository = postRepository;
        this.postImageStorage = postImageStorage;
    }

    public Post execute(CreatePostCommand command) {
        if (command == null) {
            throw new ValidationException("Create post command is required");
        }

        userAccountRepository.findById(command.authorId())
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        Post savedPost = postRepository.save(
                Post.createNew(command.authorId(), command.title(), command.content(), Instant.now())
        );

        List<UploadedFile> images = command.images() == null ? List.of() : command.images();
        if (!images.isEmpty()) {
            postImageStorage.attach(savedPost.id(), images);
        }

        return postRepository.findDetailedById(savedPost.id()).orElse(savedPost);
    }
}
