package com.Man10h.social_network_app.clean_architecture.application.usecase.post;

public record UpdatePostCommand(
        String postId,
        String actorId,
        String title,
        String content
) {
}
