package com.equipement.Repository;

import com.equipement.entity.Compte;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CompteRepository extends JpaRepository<Compte, Long> {
    // Ajoutez ici des méthodes personnalisées si nécessaire
    List<Compte> findByStructureIdStructure(Long idStructure);
}
