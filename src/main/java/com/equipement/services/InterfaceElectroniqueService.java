package com.equipement.services;

import com.equipement.entity.Compte;
import com.equipement.entity.InterfaceElectronique;
import com.equipement.Repository.CompteRepository;
import com.equipement.Repository.InterfaceElectroniqueRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Objects;

@Service
public class InterfaceElectroniqueService {

    @Autowired
    private InterfaceElectroniqueRepository interfaceElectroniqueRepository;

    @Autowired
    private CompteRepository compteRepository;

    // Méthode pour récupérer toutes les interfaces
    public List<InterfaceElectronique> getAllInterfaces() {
        return interfaceElectroniqueRepository.findAll();
    }

    // Méthode pour récupérer une interface par ID
    public InterfaceElectronique getInterfaceById(Long idInterface) {
        Objects.requireNonNull(idInterface, "L'ID de l'interface ne peut pas être null");
        return interfaceElectroniqueRepository.findById(idInterface)
                .orElseThrow(() -> new RuntimeException("Interface non trouvée avec l'ID: " + idInterface));
    }

    // Méthode pour créer une interface électronique
    public void creer(InterfaceElectronique interfaceElectronique, Long idCompte) {
        Objects.requireNonNull(interfaceElectronique, "L'interface électronique ne peut pas être null");
        Objects.requireNonNull(idCompte, "L'ID du compte ne peut pas être null");

        // Vérifier que le nom de l'interface n'est pas vide
        if (interfaceElectronique.getNom() == null || interfaceElectronique.getNom().trim().isEmpty()) {
            throw new RuntimeException("Le nom de l'interface ne peut pas être vide");
        }

        // Récupérer le compte associé
        Compte compte = compteRepository.findById(idCompte)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé"));

        // Associer l'interface au compte
        interfaceElectronique.setCompte(compte);

        // Définir la date de création
        interfaceElectronique.setDateCreation(new Date());

        // Sauvegarder l'interface électronique
        interfaceElectroniqueRepository.save(interfaceElectronique);
    }

    // Méthode pour modifier une interface électronique
    public void modifier(Long idInterface, String nouveauNom, String nouvellesObservations) {
        Objects.requireNonNull(idInterface, "L'ID de l'interface ne peut pas être null");
        Objects.requireNonNull(nouveauNom, "Le nouveau nom ne peut pas être null");

        // Récupérer l'interface électronique depuis la base de données
        InterfaceElectronique interfaceElectronique = interfaceElectroniqueRepository.findById(idInterface)
                .orElseThrow(() -> new RuntimeException("Interface électronique non trouvée"));

        // Vérifier que le nouveau nom n'est pas vide
        if (nouveauNom.trim().isEmpty()) {
            throw new RuntimeException("Le nom de l'interface ne peut pas être vide");
        }

        // Mettre à jour les propriétés de l'interface
        interfaceElectronique.setNom(nouveauNom);
        interfaceElectronique.setObservations(nouvellesObservations);

        // Sauvegarder les modifications
        interfaceElectroniqueRepository.save(interfaceElectronique);
    }

    // Méthode pour supprimer une interface électronique
    public void supprimer(Long idInterface) {
        Objects.requireNonNull(idInterface, "L'ID de l'interface ne peut pas être null");

        // Vérifier que l'interface électronique existe
        if (!interfaceElectroniqueRepository.existsById(idInterface)) {
            throw new RuntimeException("Interface électronique non trouvée");
        }

        // Supprimer l'interface électronique
        interfaceElectroniqueRepository.deleteById(idInterface);
    }
}
