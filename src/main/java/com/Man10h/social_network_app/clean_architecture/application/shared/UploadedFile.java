package com.Man10h.social_network_app.clean_architecture.application.shared;

public record UploadedFile(String originalFilename, String contentType, byte[] content) {
    public UploadedFile {
        content = content == null ? new byte[0] : content.clone();
    }

    public boolean isEmpty() {
        return content.length == 0;
    }
}
