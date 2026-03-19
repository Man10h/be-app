package com.Man10h.social_network_app.clean_architecture.application.usecase.user;

import com.Man10h.social_network_app.clean_architecture.application.shared.PageQuery;
import com.Man10h.social_network_app.clean_architecture.application.shared.PageResult;
import com.Man10h.social_network_app.clean_architecture.domain.user.entity.User;
import com.Man10h.social_network_app.clean_architecture.domain.user.repository.UserAccountRepository;

public class SearchUsersUseCase {
    private final UserAccountRepository userAccountRepository;

    public SearchUsersUseCase(UserAccountRepository userAccountRepository) {
        this.userAccountRepository = userAccountRepository;
    }

    public PageResult<User> execute(String name, PageQuery pageQuery) {
        return userAccountRepository.searchByName(name, pageQuery);
    }
}
