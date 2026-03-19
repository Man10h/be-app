package com.Man10h.social_network_app.clean_architecture.infrastructure.media;

import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

final class ByteArrayMultipartFile implements MultipartFile {
    private final String name;
    private final String contentType;
    private final byte[] content;

    ByteArrayMultipartFile(String name, String contentType, byte[] content) {
        this.name = name == null ? "file" : name;
        this.contentType = contentType;
        this.content = content == null ? new byte[0] : content.clone();
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public String getOriginalFilename() {
        return name;
    }

    @Override
    public String getContentType() {
        return contentType;
    }

    @Override
    public boolean isEmpty() {
        return content.length == 0;
    }

    @Override
    public long getSize() {
        return content.length;
    }

    @Override
    public byte[] getBytes() {
        return content.clone();
    }

    @Override
    public InputStream getInputStream() {
        return new ByteArrayInputStream(content);
    }

    @Override
    public void transferTo(java.io.File dest) throws IOException, IllegalStateException {
        java.nio.file.Files.write(dest.toPath(), content);
    }
}
