package com.baticonnecte.baticonnecte.dto;

import java.time.LocalDateTime;
import java.util.UUID;

import com.baticonnecte.baticonnecte.enumeration.StatusMetierEnum;

import lombok.Builder;

@Builder
public record MetierDtoResponse(
        UUID id,
        String nom,
        String description,
        StatusMetierEnum statut,
        LocalDateTime createdAt,
        LocalDateTime updatedAt) {

}
