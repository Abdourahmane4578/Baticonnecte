package com.baticonnecte.baticonnecte.repository;

import com.baticonnecte.baticonnecte.entity.MetierEntity;
import com.baticonnecte.baticonnecte.entity.ProfileEntity;
import com.baticonnecte.baticonnecte.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository extends JpaRepository<ProfileEntity, UUID> {

    Optional<ProfileEntity> findByUserId(UUID userId);

    Optional<ProfileEntity> findByMetier(MetierEntity metier);

    Optional<ProfileEntity> findByUser(UserEntity user);
}
