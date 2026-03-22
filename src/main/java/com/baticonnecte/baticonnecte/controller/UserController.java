package com.baticonnecte.baticonnecte.controller;

import com.baticonnecte.baticonnecte.Service.UserService;
import com.baticonnecte.baticonnecte.dto.getUserByIdResponseDto;
import com.baticonnecte.baticonnecte.entity.UserEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Utilisateur", description = "Gérer les utilisateurs")
@RestController
@RequestMapping("/api/v1/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService){
        this.userService = userService;
    }

    @Operation(summary = "Récupérer un utilisateur par id (ADMIN)")
    @ApiResponse(responseCode = "200", description = "Retourne les informations de l'utilisateur")
    @ApiResponse(responseCode = "401", description = "Accès non requise")
    @ApiResponse(responseCode = "403", description = "Authentification requise")
    @ApiResponse(responseCode = "404", description = "Utilisateur introuvable !")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/{id}")
    public ResponseEntity<getUserByIdResponseDto> getById(@PathVariable UUID id){
        UserEntity user = userService.getById(id);

        getUserByIdResponseDto userResponse =  getUserByIdResponseDto.builder()
                .id(user.getId())
                .nomComplet(user.getNomComplet())
                .adresse(user.getAdresse())
                .ville(user.getVille())
                .email(user.getEmail())
                .statut(user.getStatut())
                .role(user.getRole())
                .createdAt(user.getCreatedAt())
                .updatedAt(user.getUpdatedAt())
                .build();

        return ResponseEntity.ok(userResponse);
    }


    @Operation(summary = "Récupérer tous les utilisateurs par filtre et par pagination (ADMIN)")
    @ApiResponse(responseCode = "200", description = "Retourne les utilisateurs")
    @ApiResponse(responseCode = "401", description = "Accès non requise")
    @ApiResponse(responseCode = "403", description = "Authentification requise")
    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping()
    public Page<getUserByIdResponseDto> getAll(@RequestParam(required = false) String filter, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "10") int limit){
        Pageable pageable = PageRequest.of(page, limit);

        return userService.getAll(filter, pageable);
    }

    @Operation(summary = "Supprimer un utilisateur par son id (ADMIN)")
    @ApiResponse(responseCode = "200", description = "Utilisateur supprimé avec succès")
    @ApiResponse(responseCode = "401", description = "Accès non requise")
    @ApiResponse(responseCode = "403", description = "Authentification requise")
    @ApiResponse(responseCode = "404", description = "Utilisateur introuvable !")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id){
        userService.delete(id);
        String message = "Utilisateur supprimé avec succès !";
        return ResponseEntity.ok(message);
    }
}
