package com.Man10h.social_network_app.clean_architecture.domain.user.repository;

import com.Man10h.social_network_app.clean_architecture.domain.user.entity.UserRole;

import java.util.Optional;

public interface UserRoleRepository {
    Optional<UserRole> findDefaultRole();
}
