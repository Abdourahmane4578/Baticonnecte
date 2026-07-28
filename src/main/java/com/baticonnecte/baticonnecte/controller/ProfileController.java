package com.baticonnecte.baticonnecte.controller;

import com.baticonnecte.baticonnecte.Service.ProfileService;
import com.baticonnecte.baticonnecte.dto.CreateMetierDto;
import com.baticonnecte.baticonnecte.dto.GetProfileDto;
import com.baticonnecte.baticonnecte.entity.ProfileEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Tag(name = "Profile", description = "Gérer les profiles des ouvriers")
@RestController
@RequestMapping("/api/v1/profiles")
public class ProfileController {

    private final ProfileService profileService;

    public ProfileController(ProfileService profileService){
        this.profileService = profileService;
    }

    @Operation(summary = "Créer un profile ouvrier (OUVRIER)")
    @ApiResponse(responseCode = "201", description = "Retourne les informations du profile créé")
    @ApiResponse(responseCode = "400", description = "Champs obligatoires")
    @ApiResponse(responseCode = "401", description = "Accès non requise")
    @ApiResponse(responseCode = "403", description = "Authentification requise")
    @ApiResponse(responseCode = "404", description = "Metier ou ouvrier introuvable")
    @ApiResponse(responseCode = "409", description = "Cet ouvrier a déjà un profile !")
    @PreAuthorize("hasRole('OUVRIER')")
    @PostMapping
    public ResponseEntity<ProfileEntity> create(@Valid @RequestBody CreateMetierDto body){
        ProfileEntity profile = profileService.create(body.userId(), body.metierId(), body.description());

        return ResponseEntity.status(HttpStatus.CREATED).body(profile);
    }

    @Operation(summary = "Obtenir un profile ouvrier par l'id d'un utilisateur")
    @ApiResponse(responseCode = "200", description = "Retourne les informations du profile trouvé")
    @ApiResponse(responseCode = "403", description = "Authentification requise")
    @ApiResponse(responseCode = "404", description = "Utilisateur ou profile introuvable")
    @GetMapping("/user/{userId}")
    public ResponseEntity<GetProfileDto> getByUserId(@PathVariable UUID userId){
        return ResponseEntity.ok(profileService.getByUser(userId));
    }

    @Operation(summary = "Supprimer un profile ouvrier par son id(OUVRIER)")
    @ApiResponse(responseCode = "200", description = "Ce profile a été supprimé avec succès")
    @ApiResponse(responseCode = "401", description = "Accès non requise")
    @ApiResponse(responseCode = "403", description = "Authentification requise")
    @ApiResponse(responseCode = "404", description = "Profile introuvable")
    @PreAuthorize("hasRole('OUVRIER')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> delete(@PathVariable UUID id){
        String message = profileService.delete(id);
        return ResponseEntity.ok(message);
    }
}
