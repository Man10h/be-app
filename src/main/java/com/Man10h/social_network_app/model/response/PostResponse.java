package com.Man10h.social_network_app.model.response;

import lombok.*;

import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PostResponse {
    private String id;
    private String title;
    private String content;
    private List<ImageResponse> images;
    private List<CommentResponse> commentResponseList;
    private Long like;
}
