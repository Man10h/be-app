package com.Man10h.social_network_app.model.entity;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;

import java.util.Date;

@Entity
@Table(name = "comment")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class CommentEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "user_id")
    private String userId;

    @Column(name = "full_name")
    private String fullName;

    @Column(name = "url")
    private String url;

    @Column(name = "content")
    private String content;

    @Column(name = "create_date")
    @CreatedDate
    private Date createDate;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity postEntity;
}
