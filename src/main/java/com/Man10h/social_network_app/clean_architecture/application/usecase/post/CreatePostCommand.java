package com.Man10h.social_network_app.clean_architecture.application.usecase.post;

import com.Man10h.social_network_app.clean_architecture.application.shared.UploadedFile;

import java.util.List;

public record CreatePostCommand(
        String authorId,
        String title,
        String content,
        List<UploadedFile> images
) {
}
