package com.equipement.batch;

import com.equipement.entity.Cable;
import org.springframework.batch.item.ItemProcessor;

// Cette classe implémente ItemProcessor<Cable, Cable> pour traiter les objets Cable.
// Elle permet de valider, enrichir ou filtrer les câbles avant leur écriture.

public class CableProcessor implements ItemProcessor<Cable, Cable> {
// Méthode principale de l'interface ItemProcessor.
    // Elle prend un objet Cable en entrée et retourne un objet Cable (ou null pour filtrer).
    @Override
    public Cable process(Cable cable) throws Exception {
        System.out.println("Processing cable: " + cable.getSerialNb());

        // 1. Validation : Vérifier que les champs obligatoires sont présents.
        if (cable.getSerialNb() == null || cable.getSerialNb().isEmpty()) {
            System.err.println("Cable without serialNb, skipping");
            return null; // null = skip this item
        }

        // Normalisation du state
        // .trim() : Méthode Java qui supprime les espaces, tabulations et sauts de ligne en début et en fin de la chaîne.
        if (cable.getState() != null) {
            cable.setState(cable.getState().trim());
        } else {
            cable.setState("ko");
        }

        // Normalisation des autres champs String si nécessaire
        if (cable.getType() != null) {
            cable.setType(cable.getType().trim());
        }

        if (cable.getVersion() != null) {
            cable.setVersion(cable.getVersion().trim());
        }

        // Vous pouvez ajouter d'autres validations/transformations ici
        // Par exemple : vérifier que easting/northing sont dans une plage valide

        return cable;
    }
}