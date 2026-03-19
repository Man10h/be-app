package com.Man10h.social_network_app.clean_architecture.infrastructure.persistence;

import com.Man10h.social_network_app.clean_architecture.domain.user.entity.UserRole;
import com.Man10h.social_network_app.clean_architecture.domain.user.repository.UserRoleRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class LegacyUserRoleRepositoryAdapter implements UserRoleRepository {
    private static final long DEFAULT_USER_ROLE_ID = 2L;

    private final com.Man10h.social_network_app.repository.RoleRepository roleRepository;

    public LegacyUserRoleRepositoryAdapter(com.Man10h.social_network_app.repository.RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Optional<UserRole> findDefaultRole() {
        return roleRepository.findById(DEFAULT_USER_ROLE_ID)
                .map(role -> new UserRole(role.getId(), role.getName()));
    }
}
