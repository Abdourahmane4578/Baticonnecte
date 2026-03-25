package com.baticonnecte.baticonnecte.Service;

import com.baticonnecte.baticonnecte.entity.MetierEntity;
import com.baticonnecte.baticonnecte.enumeration.StatusMetierEnum;
import com.baticonnecte.baticonnecte.repository.MetierRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MetierService {

    private final MetierRepository metierRepository;

    public MetierService(MetierRepository metierRepository){
        this.metierRepository = metierRepository;
    }

    public MetierEntity create(String nom, String description){
        if (metierRepository.existsByNom(nom)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Ce métier existe déjà ! Veuillez saisir autre");
        }

        MetierEntity metier = MetierEntity.builder()
                .nom(nom)
                .description(description)
                .statut(StatusMetierEnum.ACTIF)
                .build();

        MetierEntity response = metierRepository.save(metier);

        return response;
    }

    public List<MetierEntity> getAll(){
        List<MetierEntity> entities = metierRepository.findAll();

        return entities;
    }

    public Optional<MetierEntity> getById(UUID id){
        if (!metierRepository.existsById(id)){
            throw new ResponseStatusException(HttpStatus.NOT_FOUND,"Metier introuvable");
        }

        return metierRepository.findById(id);
    }

    public MetierEntity update(UUID id, String nom, String description, StatusMetierEnum statut){
        MetierEntity metier = metierRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Metier introuvable"));

        if (nom != null) metier.setNom(nom);
        if (description != null) metier.setDescription(description);
        if (statut != null) metier.setStatut(statut);

        MetierEntity response = metierRepository.save(metier);

        return response;
    }

    public String delete(UUID id){
        MetierEntity metier = metierRepository.findById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Metier introuvable"));

        metierRepository.deleteById(id);

        return "Ce métier a été supprimé avec succès";
    }
}
