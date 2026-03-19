package com.Man10h.social_network_app.model.response;

import jakarta.persistence.Column;
import lombok.*;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentResponse {
    private String id;
    private String userId;
    private String fullName;
    private String url;
    private String content;
}
