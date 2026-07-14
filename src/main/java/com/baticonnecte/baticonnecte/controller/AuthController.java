package com.baticonnecte.baticonnecte.controller;

import com.baticonnecte.baticonnecte.Service.AuthService;
import com.baticonnecte.baticonnecte.config.security.JwtService;
import com.baticonnecte.baticonnecte.dto.LoginUserDto;
import com.baticonnecte.baticonnecte.dto.RegisterUserDto;
import com.baticonnecte.baticonnecte.dto.UserResponseDto;
import com.baticonnecte.baticonnecte.entity.UserEntity;
import com.baticonnecte.baticonnecte.repository.UserRepository;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.Map;

@Tag(name = "Auth", description = "Gérer l'authentification et la création d'un compte utilisateur")
@RestController()
@RequestMapping("/api/v1/auth")
public class AuthController {

        private final AuthService authService;
        private final JwtService jwtService;
        private final AuthenticationManager authenticationManager;
        private final UserRepository userRepository;

        public AuthController(AuthService authService, JwtService jwtService,
                        AuthenticationManager authenticationManager,
                        UserRepository userRepository) {
                this.authService = authService;
                this.jwtService = jwtService;
                this.authenticationManager = authenticationManager;
                this.userRepository = userRepository;
        }

        @Operation(summary = "Créer un compte utilisateur")
        @ApiResponse(responseCode = "201", description = "Retourne les informations de l'utilisateur créé")
        @ApiResponse(responseCode = "400", description = "Champs obligatoires")
        @ApiResponse(responseCode = "409", description = "Email existant !")
        @PostMapping("/register")
        public ResponseEntity<UserEntity> register(@Valid @RequestBody RegisterUserDto requestbody) {
                UserEntity userEntity = authService.register(requestbody.nomComplet(), requestbody.adresse(),
                                requestbody.ville(), requestbody.email(), requestbody.password(), requestbody.role());

                return ResponseEntity.status(201).body(userEntity);
        }

        @Operation(summary = "Authentifier un utilisateur")
        @ApiResponse(responseCode = "200", description = "Retourne les informations de l'utilisateur authentifié")
        @ApiResponse(responseCode = "400", description = "Champs obligatoires")
        @ApiResponse(responseCode = "401", description = "Utilisateur ou mot de passe incorrecte")
        @PostMapping("/login")
        public ResponseEntity<?> login(@Valid @RequestBody LoginUserDto loginUserDto) {

                Authentication authentication = authenticationManager.authenticate(
                                new UsernamePasswordAuthenticationToken(
                                                loginUserDto.email(),
                                                loginUserDto.password()));

                UserDetails userDetails = (UserDetails) authentication.getPrincipal();

                var userEntity = authService.findByEmail(userDetails.getUsername())
                                .orElseThrow(() -> new RuntimeException(
                                                "Utilisateur non trouvé après authentification"));

                String role = userDetails.getAuthorities() == null
                                ? "USER"
                                : userDetails.getAuthorities().stream()
                                                .findFirst()
                                                .map(a -> a.getAuthority().replace("ROLE_", ""))
                                                .orElse("USER");

                String token = jwtService.generateToken(
                                userEntity.getId(),
                                userEntity.getNomComplet(),
                                userDetails.getUsername(),
                                role);

                return ResponseEntity.ok(Map.of(
                                "token", token,
                                "id", userEntity.getId(),
                                "username", userEntity.getNomComplet(),
                                "email", userEntity.getEmail(),
                                "role", role));
        }

        @Operation(summary = "Récupérer le profile utilisateur")
        @ApiResponses(value = {
                @ApiResponse(responseCode = "200", description = "Retourne les informations de l'utilisateur connecté"),
                @ApiResponse(responseCode = "401", description = "Authentification requise"),
                @ApiResponse(responseCode = "404", description = "Utilisateur non trouvé")
        })
        @GetMapping("/me")
        public ResponseEntity<UserResponseDto> getCurrentUser(Authentication authentication) {

                // Vérification de l'authentification
                if (authentication == null || !authentication.isAuthenticated()) {
                        throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Authentification requise");
                }

                String email = authentication.getName();

                if (email == null || email.isEmpty()) {
                        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Email invalide");
                }

                UserEntity user = userRepository.findByEmail(email)
                        .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur non trouvé"));

                UserResponseDto dto = new UserResponseDto(
                        user.getId(),
                        user.getNomComplet(),
                        user.getEmail(),
                        user.getAdresse(),
                        user.getVille(),
                        user.getRole().name(),
                        user.getStatut(),
                        user.getCreatedAt()
                );

                return ResponseEntity.ok(dto);
        }
}
