package com.Man10h.social_network_app.clean_architecture.application.shared;

import java.util.List;

public record PageResult<T>(
        List<T> items,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
    public PageResult {
        items = items == null ? List.of() : List.copyOf(items);
    }
}
