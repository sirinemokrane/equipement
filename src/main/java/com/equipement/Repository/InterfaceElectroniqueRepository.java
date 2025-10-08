package com.equipement.Repository;

import com.equipement.entity.InterfaceElectronique;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InterfaceElectroniqueRepository extends JpaRepository<InterfaceElectronique, Long> {
    List<InterfaceElectronique> findByCompteIdCompte(Long idCompte);
}
