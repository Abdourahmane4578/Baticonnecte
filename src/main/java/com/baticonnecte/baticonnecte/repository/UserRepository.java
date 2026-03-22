package com.baticonnecte.baticonnecte.repository;

import com.baticonnecte.baticonnecte.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<UserEntity, UUID> {
    boolean existsByEmail(String email);

    Optional<UserEntity> findByEmail(String email);

    Page<UserEntity> findByNomCompletContainingIgnoreCaseOrEmailContainingIgnoreCase(String nomComplet, String email, Pageable pageable);
}
