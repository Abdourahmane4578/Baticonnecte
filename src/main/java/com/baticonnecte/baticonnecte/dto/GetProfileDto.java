package com.baticonnecte.baticonnecte.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record GetProfileDto
        (
                UUID id,
                String description,
                LocalDateTime createdAt,
                LocalDateTime updatedAt,

                UUID userId,
                String nomComplet,
                String email,
                String adresse,
                String ville,

                UUID metierId,
                String nom
        )
{
}
