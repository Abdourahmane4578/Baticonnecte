package com.baticonnecte.baticonnecte.Service;

import com.baticonnecte.baticonnecte.dto.getUserByIdResponseDto;
import com.baticonnecte.baticonnecte.entity.UserEntity;
import com.baticonnecte.baticonnecte.repository.UserRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.UUID;

@Service
public class UserService {

    private final UserRepository userRepository;

    public UserService(UserRepository userRepository){
        this.userRepository = userRepository;
    }

    public UserEntity getById(UUID id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Utilisateur introuvable"));
    }

    public Page<getUserByIdResponseDto> getAll(String filter, Pageable pageable){
        Page<UserEntity> users;

        if (filter == null || filter.isBlank()) {
            users = userRepository.findAll(pageable);
        } else {
            users = userRepository.findByNomCompletContainingIgnoreCaseOrEmailContainingIgnoreCase(
                    filter, filter, pageable
            );
        }

        return users.map(user -> new getUserByIdResponseDto(
                user.getId(),
                user.getNomComplet(),
                user.getAdresse(),
                user.getVille(),
                user.getEmail(),
                user.getStatut(),
                user.getRole(),
                user.getCreatedAt(),
                user.getUpdatedAt()
        ));
    }

    public String delete(UUID id){
        if (!userRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatusCode.valueOf(404), "Utilisateur introuvable");
        }

        userRepository.deleteById(id);

        return "Cet utilisateur a été supprimé avec succès !";
    }
}
