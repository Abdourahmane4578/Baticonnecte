package com.baticonnecte.baticonnecte.entity;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.UuidGenerator;

import java.util.UUID;

@Entity
@Table(name = "postes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PostEntity {

    @Id
    @GeneratedValue
    @UuidGenerator
    private UUID id;

    private String titre;

    @Column(length = 1000)
    private String description;

    private String image;


    @ManyToOne
    @JoinColumn(name = "user_id")
    private UserEntity user;
}