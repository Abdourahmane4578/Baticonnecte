package com.baticonnecte.baticonnecte.repository;

import com.baticonnecte.baticonnecte.entity.MetierEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface MetierRepository extends JpaRepository<MetierEntity, UUID> {

    boolean existsByNom(String nom);
}
