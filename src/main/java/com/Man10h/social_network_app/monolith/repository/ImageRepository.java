package com.Man10h.social_network_app.repository;

import com.Man10h.social_network_app.model.entity.ImageEntity;
import com.Man10h.social_network_app.model.entity.PostEntity;
import com.Man10h.social_network_app.model.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ImageRepository extends JpaRepository<ImageEntity, String> {
    void deleteByUserEntity(UserEntity userEntity);
    void deleteByPostEntity(PostEntity postEntity);
}
