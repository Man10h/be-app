package com.Man10h.social_network_app.clean_architecture.infrastructure.persistence.mapper;

import com.Man10h.social_network_app.clean_architecture.domain.post.entity.Post;
import com.Man10h.social_network_app.clean_architecture.domain.post.entity.PostComment;
import com.Man10h.social_network_app.clean_architecture.domain.shared.ImageData;
import com.Man10h.social_network_app.model.entity.CommentEntity;
import com.Man10h.social_network_app.model.entity.ImageEntity;
import com.Man10h.social_network_app.model.entity.PostEntity;
import com.Man10h.social_network_app.model.entity.UserEntity;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.Date;
import java.util.List;

@Component
public class LegacyPostMapper {
    public Post toDomain(PostEntity entity) {
        List<ImageData> images = !Hibernate.isInitialized(entity.getImageEntityList()) || entity.getImageEntityList() == null
                ? List.of()
                : entity.getImageEntityList().stream()
                .map(image -> new ImageData(image.getId(), image.getUrl()))
                .toList();

        List<PostComment> comments = !Hibernate.isInitialized(entity.getCommentEntityList()) || entity.getCommentEntityList() == null
                ? List.of()
                : entity.getCommentEntityList().stream()
                .map(this::toComment)
                .toList();

        return new Post(
                entity.getId(),
                entity.getTitle(),
                entity.getContent(),
                entity.getLikeCount() == null ? 0L : entity.getLikeCount(),
                entity.getCreateDate() == null ? Instant.now() : entity.getCreateDate().toInstant(),
                entity.getUserEntity().getId(),
                images,
                comments
        );
    }

    public PostEntity copyToEntity(Post post, PostEntity entity, UserEntity author) {
        entity.setTitle(post.title());
        entity.setContent(post.content());
        entity.setLikeCount(post.likeCount());
        entity.setCreateDate(Date.from(post.createdAt()));
        entity.setUserEntity(author);
        return entity;
    }

    private PostComment toComment(CommentEntity entity) {
        return new PostComment(
                entity.getId(),
                entity.getUserId(),
                entity.getFullName(),
                entity.getUrl(),
                entity.getContent()
        );
    }
}
