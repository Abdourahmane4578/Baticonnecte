package com.baticonnecte.baticonnecte.Service;

import com.baticonnecte.baticonnecte.dto.GetProfileDto;
import com.baticonnecte.baticonnecte.entity.MetierEntity;
import com.baticonnecte.baticonnecte.entity.ProfileEntity;
import com.baticonnecte.baticonnecte.entity.UserEntity;
import com.baticonnecte.baticonnecte.repository.MetierRepository;
import com.baticonnecte.baticonnecte.repository.ProfileRepository;
import com.baticonnecte.baticonnecte.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.Optional;
import java.util.UUID;

@Transactional
@Service
public class ProfileService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final MetierRepository metierRepository;

    public  ProfileService(ProfileRepository profileRepository, UserRepository userRepository, MetierRepository metierRepository){
        this.profileRepository = profileRepository;
        this.userRepository = userRepository;
        this.metierRepository = metierRepository;
    }

    public ProfileEntity create(UUID userId, UUID metierId, String description) {
        UserEntity user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Cet utilisateur est introuvable"));

        MetierEntity metier = metierRepository.findById(metierId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Ce metier est introuvable"));

        Optional<ProfileEntity> existingProfile = profileRepository.findByMetier(metier);
        if (existingProfile.isPresent()) {
            throw new ResponseStatusException(
                    HttpStatus.CONFLICT, "Un profil existe déjà pour ce métier");
        }

        ProfileEntity profile = ProfileEntity.builder()
                .user(user)
                .metier(metier)
                .description(description)
                .build();

        return profileRepository.save(profile);
    }

    public GetProfileDto getByUser(UUID userId){

        ProfileEntity profile = profileRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Profil introuvable pour cet utilisateur"
                ));

        UserEntity user = profile.getUser();
        MetierEntity metier = profile.getMetier();

        return new GetProfileDto(
                profile.getId(),
                profile.getDescription(),
                profile.getCreatedAt(),
                profile.getUpdatedAt(),

                user.getId(),
                user.getNomComplet(),
                user.getEmail(),
                user.getAdresse(),
                user.getVille(),

                metier != null ? metier.getId() : null,
                metier != null ? metier.getNom() : null
        );
    }

    public String delete(UUID id) {
        ProfileEntity profile = profileRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Profil introuvable"));

        if (profile.getUser() != null) {
            profile.getUser().setProfile(null);
            profile.setUser(null);
        }
        if (profile.getMetier() != null) {
            profile.getMetier().setProfile(null);
            profile.setMetier(null);
        }

        profileRepository.delete(profile);
        profileRepository.flush(); // force l'exécution du DELETE

        return "Ce profil a été supprimé avec succès";
    }

}
