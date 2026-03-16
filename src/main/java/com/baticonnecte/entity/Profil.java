package com.baticonnecte.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "profils")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Profil {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String metier;

    @Column(length = 1000)
    private String description;

    @OneToOne
    @JoinColumn(name = "user_id")
    private User user;
}