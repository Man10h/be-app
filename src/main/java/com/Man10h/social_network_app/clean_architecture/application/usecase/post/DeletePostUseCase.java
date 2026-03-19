package com.Man10h.social_network_app.clean_architecture.application.usecase.post;

import com.Man10h.social_network_app.clean_architecture.application.port.PostImageStorage;
import com.Man10h.social_network_app.clean_architecture.domain.post.entity.Post;
import com.Man10h.social_network_app.clean_architecture.domain.post.repository.PostRepository;
import com.Man10h.social_network_app.clean_architecture.domain.shared.exception.ResourceNotFoundException;
import com.Man10h.social_network_app.clean_architecture.domain.shared.exception.UnauthorizedActionException;

public class DeletePostUseCase {
    private final PostRepository postRepository;
    private final PostImageStorage postImageStorage;

    public DeletePostUseCase(PostRepository postRepository, PostImageStorage postImageStorage) {
        this.postRepository = postRepository;
        this.postImageStorage = postImageStorage;
    }

    public void execute(String postId, String actorId) {
        Post post = postRepository.findById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));

        if (!post.isOwnedBy(actorId)) {
            throw new UnauthorizedActionException("Account not authorized to delete this post");
        }

        postImageStorage.deleteAll(postId);
        postRepository.deleteById(postId);
    }
}
