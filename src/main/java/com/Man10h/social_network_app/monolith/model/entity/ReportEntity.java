package com.Man10h.social_network_app.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "report",
    uniqueConstraints = {
        @UniqueConstraint(name = "uk_report_post_user", columnNames = {"post_id", "user_id"})
    }
)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReportEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "title")
    private String title;

    @Column(name = "content")
    private String content;

    @ManyToOne
    @JoinColumn(name = "post_id")
    private PostEntity postEntity;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;
}
