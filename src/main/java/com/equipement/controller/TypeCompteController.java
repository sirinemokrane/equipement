package com.equipement.controller;

import com.equipement.entity.TypeCompte;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/api/types-compte")
public class TypeCompteController {

    @GetMapping
    public ResponseEntity<List<TypeCompte>> getAllTypesCompte() {
        List<TypeCompte> typesCompte = Arrays.asList(TypeCompte.values());
        return ResponseEntity.ok(typesCompte);
    }

    @GetMapping("/{nom}")
    public ResponseEntity<TypeCompte> getTypeCompteByNom(@PathVariable String nom) {
        try {
            TypeCompte typeCompte = TypeCompte.valueOf(nom.toUpperCase());
            return ResponseEntity.ok(typeCompte);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
