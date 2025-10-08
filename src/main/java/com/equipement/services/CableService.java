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
        System.out.println("[DEBUG] ajouterCable called with serialNb='" + cable.getSerialNb() + "'");
        // Allow serial number to be null or empty on creation.
        // Normalize empty string to null so DB stores null instead of blank.
        if (cable.getSerialNb() != null && cable.getSerialNb().trim().isEmpty()) {
            cable.setSerialNb(null);
        }
        // Ensure DB non-null column 'etat' receives a value
        if (cable.getState() == null) {
            cable.setState("ko");
        }

        // Sauvegarder le câble dans la base de données
        cableRepository.save(cable);
    }

    // Raw save without validation (used by controller for tolerant creation)
    public Cable saveRaw(Cable cable) {
        Objects.requireNonNull(cable, "Le câble ne peut pas être null");
        if (cable.getSerialNb() != null && cable.getSerialNb().trim().isEmpty()) {
            cable.setSerialNb(null);
        }
        if (cable.getState() == null) {
            cable.setState("ko");
        }
        return cableRepository.save(cable);
    }

    // Méthode pour modifier un câble
    public void modifierCable(Cable cable) {
        Objects.requireNonNull(cable, "Le câble ne peut pas être null");
        Objects.requireNonNull(cable.getIdCable(), "L'ID du câble ne peut pas être null");

        // Récupérer l'entité existante
        Cable existing = cableRepository.findById(cable.getIdCable())
                .orElseThrow(() -> new RuntimeException("Câble non trouvé avec l'ID: " + cable.getIdCable()));

        // Merge: only update fields that are provided (non-null). For serialNb, treat blank as null.
        if (cable.getSerialNb() != null) {
            String s = cable.getSerialNb().trim();
            existing.setSerialNb(s.isEmpty() ? null : s);
        }
        if (cable.getType() != null) existing.setType(cable.getType());
        if (cable.getChannelNb() != null) existing.setChannelNb(cable.getChannelNb());
        if (cable.getLineName() != null) existing.setLineName(cable.getLineName());
        if (cable.getPointNb() != null) existing.setPointNb(cable.getPointNb());
        if (cable.getState() != null) existing.setState(cable.getState());
        if (cable.getAutoTest() != null) existing.setAutoTest(cable.getAutoTest());
        if (cable.getEasting() != null) existing.setEasting(cable.getEasting());
        if (cable.getNorthing() != null) existing.setNorthing(cable.getNorthing());
        if (cable.getElevation() != null) existing.setElevation(cable.getElevation());
        if (cable.getNoise() != null) existing.setNoise(cable.getNoise());
        if (cable.getDistortion() != null) existing.setDistortion(cable.getDistortion());
        if (cable.getPhase() != null) existing.setPhase(cable.getPhase());
        if (cable.getGain() != null) existing.setGain(cable.getGain());
        if (cable.getVersion() != null) existing.setVersion(cable.getVersion());
        if (cable.getLastTestDate() != null) existing.setLastTestDate(cable.getLastTestDate());
        if (cable.getCxMaster() != null) existing.setCxMaster(cable.getCxMaster());

        // Persist merged entity
        cableRepository.save(existing);
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
    cable.setSerialNb(nouveauNumSerie);
    cable.setState(nouvelEtat ? "Ok" : "ko");

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
