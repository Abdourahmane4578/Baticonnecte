package com.baticonnecte.baticonnecte.controller;


import java.util.UUID;

import com.baticonnecte.baticonnecte.config.security.CustomUserDetails;
import com.baticonnecte.baticonnecte.dto.*;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.MediaType;
import org.springframework.web.multipart.MultipartFile;

import com.baticonnecte.baticonnecte.Service.PostService;

@Tag(name = "Post", description = "Gérer les post")
@RestController
@RequestMapping("/api/v1/posts")
public class PostController {

    private final PostService postService;

    public PostController(PostService postService) {
        this.postService = postService;
    }

    @Operation(summary = "Créer un post (CLIENT & OUVRIER)")
    @ApiResponse(responseCode = "201", description = "Retourne les informations du post créé")
    @ApiResponse(responseCode = "400", description = "champs")
    @ApiResponse(responseCode = "403", description = "Authentification requise")
    @ApiResponse(responseCode = "404", description = "Utilisateur introuvable !")
    @PreAuthorize("hasAnyRole('OUVRIER', 'CLIENT')")
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PostDto> createPost(
            @ModelAttribute CreatePostDto dto,
            @RequestParam("file") MultipartFile file
    ) {
        PostDto created = postService.createPost(dto, file);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @Operation(summary = "Récupérer tous les post par filtre et par pagination (CLIENT & OUVRIER)")
    @ApiResponse(responseCode = "200", description = "Retourne les post")
    @ApiResponse(responseCode = "401", description = "Accès non requis")
    @ApiResponse(responseCode = "403", description = "Authentification requise")
    @PreAuthorize("hasAnyRole('OUVRIER', 'CLIENT')")
    @GetMapping
    public ResponseEntity<PageResponseDto<PostResponseDto>> getAllPosts(
            @RequestParam(required = false) String filter,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit
    ) {
        Pageable pageable = PageRequest.of(page, limit);

        return ResponseEntity.ok(
                postService.getAllPosts(filter, pageable)
        );
    }

    @Operation(summary = "Récupérer un post par id (CLIENT & OUVRIER)")
    @ApiResponse(responseCode = "200", description = "Retourne les informations du post")
    @ApiResponse(responseCode = "401", description = "Accès non requise")
    @ApiResponse(responseCode = "403", description = "Authentification requise")
    @ApiResponse(responseCode = "404", description = "Post introuvable !")
    @PreAuthorize("hasAnyRole('OUVRIER', 'CLIENT')")
    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> getPostById(@PathVariable UUID id) {
        return ResponseEntity.ok(postService.getPostById(id));
    }


    @Operation(summary = "Modifier un post par id (CLIENT & OUVRIER)")
    @ApiResponse(responseCode = "200", description = "Retourne les informations du post")
    @ApiResponse(responseCode = "401", description = "Accès non requise")
    @ApiResponse(responseCode = "403", description = "Authentification requise")
    @ApiResponse(responseCode = "404", description = "Post introuvable !")
    @PreAuthorize("hasAnyRole('OUVRIER', 'CLIENT')")
    @PatchMapping(value = "/{id}", consumes = "multipart/form-data")
    public ResponseEntity<PostResponseDto> updatePost(@PathVariable UUID id,
                                              @ModelAttribute UpdatePostDto dto,
                                                      @RequestParam("file") MultipartFile file,
                                                      Authentication authentication
                                              ) {
        CustomUserDetails user =
                (CustomUserDetails) authentication.getPrincipal();
        return ResponseEntity.ok(postService.update(id, user.getId(), dto, file));
    }

    @Operation(summary = "Supprimer un post par id (CLIENT & OUVRIER)")
    @ApiResponse(responseCode = "200", description = "Retourne les informations du post")
    @ApiResponse(responseCode = "401", description = "Accès non requise")
    @ApiResponse(responseCode = "403", description = "Authentification requise")
    @ApiResponse(responseCode = "404", description = "Post introuvable !")
    @PreAuthorize("hasAnyRole('OUVRIER', 'CLIENT')")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deletePost(@PathVariable UUID id) {
        postService.deletePost(id);
        String message = "Post supprimé avec succès !";
        return ResponseEntity.ok(message);
    }
}