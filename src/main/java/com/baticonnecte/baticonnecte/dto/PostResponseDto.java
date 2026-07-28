package com.baticonnecte.baticonnecte.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record PostResponseDto(
        UUID id,
        String titre,
        String description,
        String imageUrl,

        UUID userId,
        String fullName,

        LocalDateTime createdAt,
        LocalDateTime updatedAt
) {
}
