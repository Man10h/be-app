package com.Man10h.social_network_app.clean_architecture.interfaces.rest;

import com.Man10h.social_network_app.clean_architecture.application.shared.PageResult;

import java.util.List;
import java.util.function.Function;

public record ApiPageResponse<T>(
        List<T> items,
        int page,
        int size,
        long totalElements,
        int totalPages
) {
    public static <S, T> ApiPageResponse<T> from(PageResult<S> result, Function<S, T> mapper) {
        return new ApiPageResponse<>(
                result.items().stream().map(mapper).toList(),
                result.page(),
                result.size(),
                result.totalElements(),
                result.totalPages()
        );
    }
}
