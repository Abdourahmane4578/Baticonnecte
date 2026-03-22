package com.baticonnecte.baticonnecte.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record LoginUserDto
        (
                @NotBlank(message = "L'adresse mail est requise")
                @Email(message = "L'adresse mail est invalide")
                String email,
                @NotBlank(message = "Le mot de passe est requis")
                @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
                String password
        )
{
}
