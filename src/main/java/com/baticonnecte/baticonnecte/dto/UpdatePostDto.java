package com.baticonnecte.baticonnecte.dto;

import org.springframework.web.multipart.MultipartFile;

public record UpdatePostDto(
        String titre,
        String description
) {
}
