package com.Man10h.social_network_app.clean_architecture.application.port;

import com.Man10h.social_network_app.clean_architecture.application.shared.UploadedFile;

public interface UserProfileImageStorage {
    void replace(String userId, UploadedFile image);
}
