package com.baticonnecte.baticonnecte.dto;

import java.util.List;

import lombok.Builder;

@Builder
public record PageResponseDto<T>(
        List<T> data,
        int page,
        int limit,
        long totalElements,
        int totalPages
) {

}