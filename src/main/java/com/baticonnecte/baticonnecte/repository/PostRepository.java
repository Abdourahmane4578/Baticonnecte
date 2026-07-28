package com.baticonnecte.baticonnecte.repository;

import com.baticonnecte.baticonnecte.entity.PostEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.UUID;

@Repository
public interface PostRepository extends JpaRepository<PostEntity, UUID> {
    Page<PostEntity> findByTitreContainingIgnoreCase(
            String filter,
            Pageable pageable
    );
}