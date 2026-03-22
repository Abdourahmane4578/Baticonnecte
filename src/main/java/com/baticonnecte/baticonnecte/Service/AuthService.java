package com.baticonnecte.baticonnecte.Service;

import com.baticonnecte.baticonnecte.entity.UserEntity;
import com.baticonnecte.baticonnecte.enumeration.StatusEnum;
import com.baticonnecte.baticonnecte.enumeration.TypeUserEnum;
import com.baticonnecte.baticonnecte.repository.UserRepository;
import org.springframework.http.HttpStatusCode;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;

@Service
public class AuthService {

    private final PasswordEncoder passwordEncoder;
    private final UserRepository userRepository;

    public AuthService(PasswordEncoder passwordEncoder, UserRepository userRepository){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    public UserEntity register(String nomComplet, String adresse, String ville, String email, String password, TypeUserEnum role){
        if (this.userRepository.existsByEmail(email)){
            throw new ResponseStatusException(HttpStatusCode.valueOf(409), "Cette adresse est déjà utilisé par un autre utilisateur");
        }

        UserEntity userEntity = UserEntity.builder()
                .nomComplet(nomComplet)
                .email(email)
                .ville(ville)
                .adresse(adresse)
                .password(passwordEncoder.encode(password))
                .statut(StatusEnum.ACTIF)
                .role(role)
                .build();

        UserEntity userResponse = userRepository.save(userEntity);

        return userResponse;
    }

    public Optional<UserEntity> findByEmail(String email){
        if (!userRepository.existsByEmail(email)){
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Utilisateur introuvable !");
        }

        return userRepository.findByEmail(email);
    }
}
