package com.Man10h.social_network_app.clean_architecture.domain.post.entity;

public record PostComment(String id, String userId, String fullName, String imageUrl, String content) {
}
