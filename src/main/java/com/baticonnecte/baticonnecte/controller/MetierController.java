package com.baticonnecte.baticonnecte.controller;

import com.baticonnecte.baticonnecte.Service.MetierService;
import com.baticonnecte.baticonnecte.dto.MetierDto;
import com.baticonnecte.baticonnecte.dto.MetierDtoResponse;
import com.baticonnecte.baticonnecte.entity.MetierEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Tag(name = "Metier", description = "Gérer les metiers")
@RestController
@RequestMapping("/api/v1/metiers")
public class MetierController {

    private final MetierService metierService;

    public MetierController(MetierService metierService) {
        this.metierService = metierService;
    }

    @Operation(summary = "Créer un métier (ADMIN)")
    @ApiResponse(responseCode = "201", description = "Retourne les informations du métier créé")
    @ApiResponse(responseCode = "400", description = "Champs obligatoires")
    @ApiResponse(responseCode = "401", description = "Accès non requise")
    @ApiResponse(responseCode = "403", description = "Authentification requise")
    @ApiResponse(responseCode = "409", description = "Ce métier existe !")
    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping()
    public ResponseEntity<MetierEntity> create(@Valid @RequestBody MetierDto body) {
        MetierEntity response = metierService.create(body.nom(), body.description());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Créer un métier par id")
    @ApiResponse(responseCode = "200", description = "Retourne les informations du métier trouvée")
    @ApiResponse(responseCode = "403", description = "Authentification requise")
    @ApiResponse(responseCode = "404", description = "Ce métier est introuvable !")
    @GetMapping("/{id}")
    public ResponseEntity<Optional<MetierEntity>> getById(@PathVariable UUID id) {
        Optional<MetierEntity> metierEntity = metierService.getById(id);

        return ResponseEntity.ok(metierEntity);
    }

    @Operation(summary = "Lister les métiers")
    @ApiResponse(responseCode = "200", description = "Retourne la liste des métiers")
    @ApiResponse(responseCode = "403", description = "Authentification requise")
    @GetMapping
    public ResponseEntity<List<MetierDtoResponse>> getAll() {
        return ResponseEntity.ok(metierService.getAll());
    }

    @Operation(summary = "Modifier un métier par id (ADMIN)")
    @ApiResponse(responseCode = "200", description = "Retourner un metierr modifié par son id")
    @ApiResponse(responseCode = "401", description = "Accès non requise")
    @ApiResponse(responseCode = "403", description = "Authentification requise")
    @ApiResponse(responseCode = "404", description = "Ce métier est introuvable !")
    @PreAuthorize("hasRole('ADMIN')")
    @PatchMapping("/{id}")
    public ResponseEntity<MetierEntity> update(@PathVariable UUID id, @RequestBody MetierDto body) {
        MetierEntity metierEntity = metierService.update(id, body.nom(), body.description(), body.statut());

        return ResponseEntity.ok(metierEntity);
    }

    @Operation(summary = "Supprimer un métier par id (ADMIN)")
    @ApiResponse(responseCode = "200", description = "Ce metier a été supprimé avec succès")
    @ApiResponse(responseCode = "401", description = "Accès non requise")
    @ApiResponse(responseCode = "403", description = "Authentification requise")
    @ApiResponse(responseCode = "404", description = "Ce métier est introuvable !")
    @PreAuthorize("hasRole('ADMIN')")
    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id) {

        return ResponseEntity.ok(metierService.delete(id));
    }
}
