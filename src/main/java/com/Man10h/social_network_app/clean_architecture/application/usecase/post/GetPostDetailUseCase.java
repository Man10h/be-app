package com.Man10h.social_network_app.clean_architecture.application.usecase.post;

import com.Man10h.social_network_app.clean_architecture.domain.post.entity.Post;
import com.Man10h.social_network_app.clean_architecture.domain.post.repository.PostRepository;
import com.Man10h.social_network_app.clean_architecture.domain.shared.exception.ResourceNotFoundException;

public class GetPostDetailUseCase {
    private final PostRepository postRepository;

    public GetPostDetailUseCase(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public Post execute(String postId) {
        return postRepository.findDetailedById(postId)
                .orElseThrow(() -> new ResourceNotFoundException("Post not found"));
    }
}
