package com.Man10h.social_network_app.clean_architecture.application.port;

import com.Man10h.social_network_app.clean_architecture.application.shared.UploadedFile;

import java.util.List;

public interface PostImageStorage {
    void attach(String postId, List<UploadedFile> images);

    void deleteAll(String postId);
}
