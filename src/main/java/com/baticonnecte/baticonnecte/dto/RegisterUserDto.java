package com.baticonnecte.baticonnecte.dto;

import com.baticonnecte.baticonnecte.enumeration.TypeUserEnum;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

public record RegisterUserDto
        (
                @NotBlank(message = "Le nom complet est requis")
                String nomComplet,
                @NotBlank(message = "L'adresse est requise")
                String adresse,
                @NotBlank(message = "La ville est requise")
                String ville,
                @NotBlank(message = "L'adresse mail est requise")
                @Email(message = "L'adresse mail invalide")
                String email,
                @NotBlank(message = "Le mot de passe est requise")
                @Size(min = 6, message = "Le mot de passe doit contenir au moins 6 caractères")
                String password,
                @NotNull(message = "Le rôle de l'utilisateur est requis")
                TypeUserEnum role
        )
{
}
