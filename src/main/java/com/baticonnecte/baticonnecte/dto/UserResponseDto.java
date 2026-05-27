package com.baticonnecte.baticonnecte.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.baticonnecte.baticonnecte.enumeration.StatusEnum;

public record UserResponseDto(
        UUID id,
        String nomComplet,
        String email,
        String adresse,
        String ville,
        String role,
        StatusEnum statut,
        LocalDateTime createdAt) {
}
