package com.Man10h.social_network_app.model.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(
        name = "follower",
        uniqueConstraints = {
                @UniqueConstraint(
                        name = "uk_user_follower",
                        columnNames = {"user_id", "follower_id"}
                )
        }
)
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class FollowerEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(name = "follower_id")
    private String followerId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity userEntity;
}
