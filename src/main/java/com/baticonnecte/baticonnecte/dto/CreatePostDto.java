package com.baticonnecte.baticonnecte.dto;

import java.util.UUID;

public record CreatePostDto(
        String titre,
        String description,
        String imageUrl,
        UUID userId
)
{

}
