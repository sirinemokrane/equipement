package com.equipement.batch;

import com.equipement.Repository.CableRepository;
import com.equipement.entity.Cable;

import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;

public class CableWriter implements ItemWriter<Cable> {

    private final CableRepository cableRepository;

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