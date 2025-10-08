package com.equipement.batch;

import com.equipement.Repository.CableRepository;
import com.equipement.entity.Cable;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

// Cette classe implémente l’interface `ItemWriter<T>` fournie par **Spring Batch**.
//- Le type générique `<Cable>` indique que ton writer gère des objets de type `Cable`.
//- Le rôle principal d’un `ItemWriter` est d’écrire (sauvegarder) une **liste d’éléments** après qu’ils ont été lus et transformés.
public class CableWriter implements ItemWriter<Cable> {

    private final CableRepository cableRepository;

//’est le **constructeur** de la classe.
//- Il reçoit une instance de `CableRepository` (injection de dépendance par constructeur).
    public CableWriter(CableRepository cableRepository) {
        this.cableRepository = cableRepository;
    }

    @Override
    public void write(Chunk<? extends Cable> chunk) throws Exception {
        System.out.println("Writing chunk of " + chunk.size() + " cables");

        // Sauvegarder tous les câbles du chunk en une seule transaction
        cableRepository.saveAll(chunk.getItems());

        System.out.println("Successfully saved " + chunk.size() + " cables");
    }
}