package com.baticonnecte.baticonnecte.dto;

import java.util.UUID;

public record PostDto(
        UUID id,
        String titre,
        String description,
        String image,
        UUID userId
)
{

}
