package com.equipement.batch;

import com.equipement.entity.Cable;
import com.equipement.Repository.CableRepository;
import org.springframework.batch.item.Chunk;
import org.springframework.batch.item.ItemWriter;
import org.springframework.lang.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class DBWriter implements ItemWriter<Cable> {
    private final CableRepository cableRepository;

    @Autowired
    public DBWriter(CableRepository cableRepository) {
        this.cableRepository = cableRepository;
    }

    @Override
    public void write(@NonNull Chunk<? extends Cable> chunk) throws Exception {
        java.util.List<? extends Cable> items = chunk.getItems();
        if (items == null || items.isEmpty()) return;
        System.out.println("[DBWriter] Data Saved for Cables: count=" + items.size());
        cableRepository.saveAll(items);
    }

}
