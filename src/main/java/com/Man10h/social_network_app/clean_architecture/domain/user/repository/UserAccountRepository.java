package com.Man10h.social_network_app.clean_architecture.domain.user.repository;

import com.Man10h.social_network_app.clean_architecture.application.shared.PageQuery;
import com.Man10h.social_network_app.clean_architecture.application.shared.PageResult;
import com.Man10h.social_network_app.clean_architecture.domain.user.entity.User;

import java.util.Optional;

public interface UserAccountRepository {
    Optional<User> findById(String id);

    Optional<User> findProfileById(String id);

    Optional<User> findByUsername(String username);

    Optional<User> findByEmail(String email);

    User save(User user);

    PageResult<User> searchByName(String name, PageQuery pageQuery);
}
