package com.baticonnecte.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "messages")

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Message {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 1000)
    private String contenu;

    private LocalDateTime dateEnvoi;

    @ManyToOne
    @JoinColumn(name = "expediteur_id")
    private User expediteur;

    @ManyToOne
    @JoinColumn(name = "destinataire_id")
    private User destinataire;
}