package com.Man10h.social_network_app.clean_architecture.domain.post.repository;

import com.Man10h.social_network_app.clean_architecture.application.shared.PageQuery;
import com.Man10h.social_network_app.clean_architecture.application.shared.PageResult;
import com.Man10h.social_network_app.clean_architecture.domain.post.entity.Post;

import java.util.Optional;

public interface PostRepository {
    Post save(Post post);

    Optional<Post> findById(String id);

    Optional<Post> findDetailedById(String id);

    PageResult<Post> findFollowerPosts(String userId, PageQuery pageQuery);

    PageResult<Post> findByAuthorId(String userId, PageQuery pageQuery);

    void deleteById(String id);
}
