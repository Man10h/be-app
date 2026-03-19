package com.Man10h.social_network_app.clean_architecture.infrastructure.persistence.mapper;

import com.Man10h.social_network_app.clean_architecture.domain.shared.ImageData;
import com.Man10h.social_network_app.clean_architecture.domain.user.entity.User;
import com.Man10h.social_network_app.clean_architecture.domain.user.entity.UserRole;
import com.Man10h.social_network_app.model.entity.ImageEntity;
import com.Man10h.social_network_app.model.entity.RoleEntity;
import com.Man10h.social_network_app.model.entity.UserEntity;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class LegacyUserMapper {
    public User toDomain(UserEntity entity) {
        ImageData profileImage = null;
        List<ImageEntity> images = Hibernate.isInitialized(entity.getImageEntityList())
                ? entity.getImageEntityList()
                : List.of();
        if (!images.isEmpty()) {
            ImageEntity imageEntity = images.get(0);
            profileImage = new ImageData(imageEntity.getId(), imageEntity.getUrl());
        }

        RoleEntity roleEntity = entity.getRoleEntity();
        UserRole role = roleEntity == null ? null : new UserRole(roleEntity.getId(), roleEntity.getName());

        return new User(
                entity.getId(),
                entity.getUsername(),
                entity.getEmail(),
                entity.getPassword(),
                entity.getFirstName(),
                entity.getLastName(),
                entity.getGender(),
                entity.isEnabled(),
                role,
                profileImage
        );
    }

    public UserEntity copyToEntity(User user, UserEntity entity, RoleEntity roleEntity) {
        entity.setUsername(user.username());
        entity.setEmail(user.email());
        entity.setPassword(user.passwordHash());
        entity.setFirstName(user.firstName());
        entity.setLastName(user.lastName());
        entity.setGender(user.gender());
        entity.setEnabled(user.enabled());
        entity.setRoleEntity(roleEntity);
        return entity;
    }
}
