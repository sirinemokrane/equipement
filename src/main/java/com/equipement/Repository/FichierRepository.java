package com.equipement.Repository;

import com.equipement.entity.Fichier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FichierRepository extends JpaRepository<Fichier, Long> {
    List<Fichier> findByInterfaceElectroniqueIdInterface(Long idInterface);
}
