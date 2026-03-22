package com.baticonnecte.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "metiers")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Metier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_metier")
    private Long id;

    private String description;

    private String statut;

}