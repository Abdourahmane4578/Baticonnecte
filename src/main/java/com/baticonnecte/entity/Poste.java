package com.baticonnecte.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "postes")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Poste {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_poste")
    private Long id;

    private String titre;

    @Column(length = 2000)
    private String description;

    private String image;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}