package com.baticonnecte.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "users")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nomcomplet;

    private String adresse;

    @Column(unique = true, nullable = false)
    private String email;

    private String password;

    private String telephone;

    private String ville;

    private String type;

    private String statut;
    @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
    private Profil profil;
}