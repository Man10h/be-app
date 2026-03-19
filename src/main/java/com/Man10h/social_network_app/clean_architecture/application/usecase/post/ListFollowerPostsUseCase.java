package com.Man10h.social_network_app.clean_architecture.application.usecase.post;

import com.Man10h.social_network_app.clean_architecture.application.shared.PageQuery;
import com.Man10h.social_network_app.clean_architecture.application.shared.PageResult;
import com.Man10h.social_network_app.clean_architecture.domain.post.entity.Post;
import com.Man10h.social_network_app.clean_architecture.domain.post.repository.PostRepository;

public class ListFollowerPostsUseCase {
    private final PostRepository postRepository;

    public ListFollowerPostsUseCase(PostRepository postRepository) {
        this.postRepository = postRepository;
    }

    public PageResult<Post> execute(String userId, PageQuery pageQuery) {
        return postRepository.findFollowerPosts(userId, pageQuery);
    }
}
