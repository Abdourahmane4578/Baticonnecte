package com.baticonnecte.baticonnecte.dto;

import com.baticonnecte.baticonnecte.enumeration.StatusMetierEnum;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record MetierDto
        (
                @NotBlank(message = "Le nom du métier est requis")
                String nom,
                String description,
                StatusMetierEnum statut
        )
{
}
