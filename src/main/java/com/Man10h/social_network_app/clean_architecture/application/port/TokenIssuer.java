package com.Man10h.social_network_app.clean_architecture.application.port;

import com.Man10h.social_network_app.clean_architecture.domain.user.entity.User;

public interface TokenIssuer {
    String issue(User user);
}
