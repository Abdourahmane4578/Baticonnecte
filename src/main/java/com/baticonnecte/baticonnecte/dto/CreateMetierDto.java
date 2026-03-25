package com.baticonnecte.baticonnecte.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record CreateMetierDto
        (
                @NotNull(message = "L'id de l'utilisateur est requis")
                UUID userId,
                @NotNull(message = "L'id du metier est requis")
                UUID metierId,
                @NotNull(message = "La description du profile est requise")
                String description
        )
{
}
