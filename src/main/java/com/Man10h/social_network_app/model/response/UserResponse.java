package com.Man10h.social_network_app.model.response;

import lombok.*;
import org.springframework.web.multipart.MultipartFile;
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String gender;
    private ImageResponse image;
}
