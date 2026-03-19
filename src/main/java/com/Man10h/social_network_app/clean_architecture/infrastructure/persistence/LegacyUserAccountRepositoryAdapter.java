package com.Man10h.social_network_app.clean_architecture.infrastructure.persistence;

import com.Man10h.social_network_app.clean_architecture.application.shared.PageQuery;
import com.Man10h.social_network_app.clean_architecture.application.shared.PageResult;
import com.Man10h.social_network_app.clean_architecture.domain.shared.exception.ValidationException;
import com.Man10h.social_network_app.clean_architecture.domain.user.entity.User;
import com.Man10h.social_network_app.clean_architecture.domain.user.entity.UserRole;
import com.Man10h.social_network_app.clean_architecture.domain.user.repository.UserAccountRepository;
import com.Man10h.social_network_app.clean_architecture.infrastructure.persistence.mapper.LegacyUserMapper;
import com.Man10h.social_network_app.model.entity.RoleEntity;
import com.Man10h.social_network_app.model.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class LegacyUserAccountRepositoryAdapter implements UserAccountRepository {
    private final com.Man10h.social_network_app.repository.UserRepository userRepository;
    private final com.Man10h.social_network_app.repository.RoleRepository roleRepository;
    private final LegacyUserMapper legacyUserMapper;

    public LegacyUserAccountRepositoryAdapter(
            com.Man10h.social_network_app.repository.UserRepository userRepository,
            com.Man10h.social_network_app.repository.RoleRepository roleRepository,
            LegacyUserMapper legacyUserMapper
    ) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.legacyUserMapper = legacyUserMapper;
    }

    @Override
    public Optional<User> findById(String id) {
        return userRepository.findById(id).map(legacyUserMapper::toDomain);
    }

    @Override
    public Optional<User> findProfileById(String id) {
        return userRepository.getProfile(id).map(legacyUserMapper::toDomain);
    }

    @Override
    public Optional<User> findByUsername(String username) {
        return userRepository.findByUsername(username).map(legacyUserMapper::toDomain);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email).map(legacyUserMapper::toDomain);
    }

    @Override
    public User save(User user) {
        UserEntity entity = user.id() == null
                ? new UserEntity()
                : userRepository.findById(user.id()).orElse(new UserEntity());

        RoleEntity roleEntity = resolveRole(user.role());
        UserEntity savedEntity = userRepository.save(legacyUserMapper.copyToEntity(user, entity, roleEntity));
        return legacyUserMapper.toDomain(savedEntity);
    }

    @Override
    public PageResult<User> searchByName(String name, PageQuery pageQuery) {
        Page<UserEntity> page = userRepository.findUsersByName(name, PageRequest.of(pageQuery.page(), pageQuery.size()));
        return new PageResult<>(
                page.getContent().stream().map(legacyUserMapper::toDomain).toList(),
                pageQuery.page(),
                pageQuery.size(),
                page.getTotalElements(),
                page.getTotalPages()
        );
    }

    private RoleEntity resolveRole(UserRole role) {
        if (role == null) {
            throw new ValidationException("Role is required");
        }
        return roleRepository.findById(role.id())
                .orElseThrow(() -> new ValidationException("Role not found: " + role.id()));
    }
}
