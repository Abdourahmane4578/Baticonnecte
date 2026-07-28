package com.baticonnecte.baticonnecte.dto;

import lombok.Builder;

@Builder
public record ErrorResponseDto(
        String message,
        String error,
        int statusCode
) {
}
