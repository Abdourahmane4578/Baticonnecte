package com.baticonnecte.baticonnecte.dto;

import com.baticonnecte.baticonnecte.enumeration.StatusEnum;
import com.baticonnecte.baticonnecte.enumeration.TypeUserEnum;
import lombok.Builder;

import java.time.LocalDateTime;
import java.util.UUID;

@Builder
public record getUserByIdResponseDto
        (
                UUID id,
                String nomComplet,
                String adresse,
                String ville,
                String email,
                StatusEnum statut,
                TypeUserEnum role,
                LocalDateTime createdAt,
                LocalDateTime updatedAt
        )
{
}
