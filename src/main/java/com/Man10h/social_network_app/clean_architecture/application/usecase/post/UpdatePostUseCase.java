package com.Man10h.social_network_app.clean_architecture.application.usecase.post;

import com.Man10h.social_network_app.clean_architecture.domain.post.entity.Post;
import com.Man10h.social_network_app.clean_architecture.domain.post.repository.PostRepository;
import com.Man10h.social_network_app.clean_architecture.domain.shared.exception.ResourceNotFoundException;
import com.Man10h.social_network_app.clean_architecture.domain.shared.exception.UnauthorizedActionException;
import com.Man10h.social_network_app.clean_architecture.domain.shared.exception.ValidationException;

public class UpdatePostUseCase {
    private final PostRepository postRepository;

    public UpdatePostUseCase(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post execute(UpdatePostCommand command) {
        if (command == null) {
            throw new ValidationException("Update post command is required");
        }

        Post post = postRepository.findById(command.postId())
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        if (!post.isOwnedBy(command.actorId())) {
            throw new UnauthorizedActionException("Account not authorized to update this post");
        }

        Post updatedPost = post.update(command.title(), command.content());
        Post savedPost = postRepository.save(updatedPost);
        return postRepository.findDetailedById(savedPost.id()).orElse(savedPost);
    }
}
