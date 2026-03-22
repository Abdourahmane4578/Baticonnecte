package com.baticonnecte.baticonnecte.controller;

import com.baticonnecte.baticonnecte.Service.MetierService;
import com.baticonnecte.baticonnecte.dto.MetierDto;
import com.baticonnecte.baticonnecte.entity.MetierEntity;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/metiers")
public class MetierController {

    private final MetierService metierService;

    public MetierController(MetierService metierService){
        this.metierService = metierService;
    }

    @PostMapping()
    public ResponseEntity<MetierEntity> create(@Valid @RequestBody MetierDto body){
        MetierEntity response = metierService.create(body.nom(), body.description());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Optional<MetierEntity>> getById(@PathVariable UUID id){
        Optional<MetierEntity> metierEntity = metierService.getById(id);

        return ResponseEntity.ok(metierEntity);
    }

    @GetMapping()
    public ResponseEntity<List<MetierEntity>> getAll(){
        List<MetierEntity> metierEntities = metierService.getAll();

        return ResponseEntity.ok(metierEntities);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<MetierEntity> update(@PathVariable UUID id, @RequestBody MetierDto body){
        MetierEntity metierEntity = metierService.update(id, body.nom(), body.description());

        return ResponseEntity.ok(metierEntity);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable UUID id){

        return ResponseEntity.ok(metierService.delete(id));
    }
}
