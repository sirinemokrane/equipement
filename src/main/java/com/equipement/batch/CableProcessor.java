package com.equipement.batch;

import com.equipement.entity.Cable;
import org.springframework.batch.item.ItemProcessor;

public class CableProcessor implements ItemProcessor<Cable, Cable> {

    @Override
    public Cable process(Cable cable) throws Exception {
        System.out.println("Processing cable: " + cable.getSerialNb());

        // Validation basique
        if (cable.getSerialNb() == null || cable.getSerialNb().isEmpty()) {
            System.err.println("Cable without serialNb, skipping");
            return null; // null = skip this item
        }

        // Normalisation du state
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