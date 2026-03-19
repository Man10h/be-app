package com.Man10h.social_network_app.clean_architecture.domain.post.entity;

import com.Man10h.social_network_app.clean_architecture.domain.shared.ImageData;

import java.time.Instant;
import java.util.List;

public final class Post {
    private final String id;
    private final String title;
    private final String content;
    private final long likeCount;
    private final Instant createdAt;
    private final String authorId;
    private final List<ImageData> images;
    private final List<PostComment> comments;

    public Post(
            String id,
            String title,
            String content,
            long likeCount,
            Instant createdAt,
            String authorId,
            List<ImageData> images,
            List<PostComment> comments
    ) {
        this.id = id;
        this.title = normalize(title);
        this.content = normalize(content);
        this.likeCount = likeCount;
        this.createdAt = createdAt == null ? Instant.now() : createdAt;
        this.authorId = requireValue(authorId, "Author");
        this.images = images == null ? List.of() : List.copyOf(images);
        this.comments = comments == null ? List.of() : List.copyOf(comments);
    }

    public static Post createNew(String authorId, String title, String content, Instant createdAt) {
        return new Post(null, title, content, 0L, createdAt, authorId, List.of(), List.of());
    }

    public Post update(String title, String content) {
        return new Post(
                id,
                title == null || title.isBlank() ? this.title : title.trim(),
                content == null || content.isBlank() ? this.content : content.trim(),
                likeCount,
                createdAt,
                authorId,
                images,
                comments
        );
    }

    public Post withAssets(List<ImageData> images, List<PostComment> comments) {
        return new Post(id, title, content, likeCount, createdAt, authorId, images, comments);
    }

    public boolean isOwnedBy(String userId) {
        return authorId.equals(userId);
    }

    public String id() {
        return id;
    }

    public String title() {
        return title;
    }

    public String content() {
        return content;
    }

    public long likeCount() {
        return likeCount;
    }

    public Instant createdAt() {
        return createdAt;
    }

    public String authorId() {
        return authorId;
    }

    public List<ImageData> images() {
        return images;
    }

    public List<PostComment> comments() {
        return comments;
    }

    private static String requireValue(String value, String fieldName) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException(fieldName + " is required");
        }
        return value.trim();
    }

    private static String normalize(String value) {
        return value == null ? "" : value.trim();
    }
}
