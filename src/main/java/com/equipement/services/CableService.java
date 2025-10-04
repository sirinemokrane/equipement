package com.equipement.services;

import com.equipement.entity.Cable;
import com.equipement.Repository.CableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CableService {

    @Autowired
    private CableRepository cableRepository;

    // Méthode pour récupérer tous les câbles
    public List<Cable> getAllCables() {
        return cableRepository.findAll();
    }

    // Méthode pour récupérer un câble par ID
    public Cable getCableById(Long idCable) {
        Objects.requireNonNull(idCable, "L'ID du câble ne peut pas être null");
        return cableRepository.findById(idCable)
                .orElseThrow(() -> new RuntimeException("Câble non trouvé avec l'ID: " + idCable));
    }

    // Méthode pour ajouter un câble
    public void ajouterCable(Cable cable) {
        Objects.requireNonNull(cable, "Le câble ne peut pas être null");
        Objects.requireNonNull(cable.getNumSerie(), "Le numéro de série ne peut pas être null");

        // Valider que le numéro de série n'est pas vide
        if (cable.getNumSerie().trim().isEmpty()) {
            throw new RuntimeException("Le numéro de série ne peut pas être vide");
        }

        // Sauvegarder le câble dans la base de données
        cableRepository.save(cable);
    }

    // Méthode pour modifier un câble
    public void modifierCable(Cable cable) {
        Objects.requireNonNull(cable, "Le câble ne peut pas être null");
        Objects.requireNonNull(cable.getIdCable(), "L'ID du câble ne peut pas être null");
        Objects.requireNonNull(cable.getNumSerie(), "Le numéro de série ne peut pas être null");

        // Vérifier que le câble existe
        if (!cableRepository.existsById(cable.getIdCable())) {
            throw new RuntimeException("Câble non trouvé avec l'ID: " + cable.getIdCable());
        }

        // Valider le numéro de série
        if (cable.getNumSerie().trim().isEmpty()) {
            throw new RuntimeException("Le numéro de série ne peut pas être vide");
        }

        // Sauvegarder les modifications
        cableRepository.save(cable);
    }

    // Méthode pour modifier un câble (ancienne version - gardée pour compatibilité)
    public void modifierCable(Long idCable, String nouveauNumSerie, boolean nouvelEtat) {
        Objects.requireNonNull(idCable, "L'ID du câble ne peut pas être null");
        Objects.requireNonNull(nouveauNumSerie, "Le nouveau numéro de série ne peut pas être null");

        // Récupérer le câble depuis la base de données
        Cable cable = cableRepository.findById(idCable)
                .orElseThrow(() -> new RuntimeException("Câble non trouvé"));

        // Valider le nouveau numéro de série
        if (nouveauNumSerie.trim().isEmpty()) {
            throw new RuntimeException("Le numéro de série ne peut pas être vide");
        }

        // Mettre à jour les propriétés du câble
        cable.setNumSerie(nouveauNumSerie);
        cable.setEtat(nouvelEtat);

        // Sauvegarder les modifications
        cableRepository.save(cable);
    }

    // Méthode pour supprimer un câble
    public void supprimerCable(Long idCable) {
        Objects.requireNonNull(idCable, "L'ID du câble ne peut pas être null");

        // Vérifier que le câble existe
        if (!cableRepository.existsById(idCable)) {
            throw new RuntimeException("Câble non trouvé");
        }

        // Supprimer le câble
        cableRepository.deleteById(idCable);
    }
}
