package com.baticonnecte.baticonnecte.dto;

import jakarta.validation.constraints.NotBlank;

public record MetierDto
        (
                @NotBlank(message = "Le nom du métier est requis")
                String nom,

                String description
        )
{
}
