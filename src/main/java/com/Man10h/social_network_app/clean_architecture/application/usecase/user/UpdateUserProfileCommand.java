package com.Man10h.social_network_app.clean_architecture.application.usecase.user;

import com.Man10h.social_network_app.clean_architecture.application.shared.UploadedFile;

public record UpdateUserProfileCommand(
        String userId,
        String firstName,
        String lastName,
        String gender,
        UploadedFile image
) {
}
