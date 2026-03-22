package com.baticonnecte.baticonnecte.controller;

import com.baticonnecte.baticonnecte.Service.AuthService;
import com.baticonnecte.baticonnecte.config.security.JwtService;
import com.baticonnecte.baticonnecte.dto.LoginUserDto;
import com.baticonnecte.baticonnecte.dto.RegisterUserDto;
import com.baticonnecte.baticonnecte.entity.UserEntity;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@Tag(name = "Auth", description = "Gérer l'authentification et la création d'un compte utilisateur")
@RestController()
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthController(AuthService authService, JwtService jwtService, AuthenticationManager authenticationManager){
        this.authService = authService;
        this.jwtService = jwtService;
        this.authenticationManager = authenticationManager;
    }

    @Operation(summary = "Créer un compte utilisateur")
    @ApiResponse(responseCode = "201", description = "Retourne les informations de l'utilisateur créé")
    @ApiResponse(responseCode = "400", description = "Champs obligatoires")
    @ApiResponse(responseCode = "409", description = "Email existant !")
    @PostMapping("/register")
    public ResponseEntity<UserEntity> register(@Valid @RequestBody RegisterUserDto requestbody){
        UserEntity userEntity = authService.register(requestbody.nomComplet(), requestbody.adresse(), requestbody.ville(), requestbody.email(), requestbody.password(), requestbody.role());

        return ResponseEntity.status(201).body(userEntity);
    }

    @Operation(summary = "Authentifier un utilisateur")
    @ApiResponse(responseCode = "200", description = "Retourne les informations de l'utilisateur authentifié")
    @ApiResponse(responseCode = "400", description = "Champs obligatoires")
    @ApiResponse(responseCode = "401", description = "Utilisateur ou mot de passe incorrecte")
    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginUserDto loginUserDto){
        Authentication authentication = authenticationManager.authenticate(
            new UsernamePasswordAuthenticationToken(
                    loginUserDto.email(),
                    loginUserDto.password()
            )
        );

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        var userEntity = authService.findByEmail(userDetails.getUsername())
                .orElseThrow(() -> new RuntimeException("Utilisateur non trouvé après authentification"));

        String role = userDetails.getAuthorities().stream()
                .findFirst()
                .map(item -> item.getAuthority().replace("ROLE_", ""))
                .orElse("USER");

        String token = jwtService.generateToken(userDetails.getUsername(), role);

        return ResponseEntity.ok(Map.of(
                "token", token,
                "id", userEntity.getId(),
                "username", userEntity.getNomComplet(),
                "email", userEntity.getEmail(),
                "role", role));
    }
}
