package com.equipement.services;

import com.equipement.entity.Compte;
import com.equipement.entity.TypeCompte;
import com.equipement.Repository.CompteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class CompteService {

    @Autowired
    private CompteRepository compteRepository;

    // Méthodes CRUD de base
    public List<Compte> getAllComptes() {
        return compteRepository.findAll();
    }

    public Compte getCompteById(Long id) {
        return compteRepository.findById(id).orElse(null);
    }

    public Compte saveCompte(Compte compte) {
        return compteRepository.save(compte);
    }

    public void deleteCompte(Long id) {
        compteRepository.deleteById(id);
    }

    public List<Compte> getComptesByStructure(Long idStructure) {
        return compteRepository.findByStructureIdStructure(idStructure);
    }

    // Méthode pour obtenir le rôle d'un compte
    public TypeCompte obtenirRole(Long idCompte) {
        Compte compte = compteRepository.findById(idCompte)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé"));
        return compte.getTypeCompte();
    }

    // Méthode pour authentifier un compte
    public boolean authentifier(Long idCompte, String motDePasseSaisi) {
        Compte compte = compteRepository.findById(idCompte)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé"));
        return compte.getMotDePasse().equals(motDePasseSaisi);
    }

    // Méthode pour changer le mot de passe
    public void changerMotDePasse(Long idCompte, String ancienMotDePasse, String nouveauMotDePasse) {
        // Vérifier que les paramètres ne sont pas nuls
        Objects.requireNonNull(ancienMotDePasse, "L'ancien mot de passe ne peut pas être null");
        Objects.requireNonNull(nouveauMotDePasse, "Le nouveau mot de passe ne peut pas être null");

        Compte compte = compteRepository.findById(idCompte)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé"));

        // Vérifier l'ancien mot de passe
        if (!compte.getMotDePasse().equals(ancienMotDePasse)) {
            throw new RuntimeException("Ancien mot de passe incorrect");
        }

        // Valider le nouveau mot de passe (exemple : longueur minimale)
        if (nouveauMotDePasse.length() < 8) {
            throw new RuntimeException("Le mot de passe doit contenir au moins 8 caractères");
        }

        // Mettre à jour le mot de passe
        compte.setMotDePasse(nouveauMotDePasse);
        compteRepository.save(compte);
    }

    // Méthode pour mettre à jour le profil d'un compte
    public void mettreAJourProfil(Long idCompte, String nouveauNom, String nouvelEmail) {
        // Vérifier que les paramètres ne sont pas nuls
        Objects.requireNonNull(nouveauNom, "Le nom ne peut pas être null");
        Objects.requireNonNull(nouvelEmail, "L'email ne peut pas être null");

        Compte compte = compteRepository.findById(idCompte)
                .orElseThrow(() -> new RuntimeException("Compte non trouvé"));

        // Valider le nom
        if (nouveauNom.trim().isEmpty()) {
            throw new RuntimeException("Le nom ne peut pas être vide");
        }

        // Valider l'email (exemple : vérifier la présence de @)
        if (!nouvelEmail.contains("@")) {
            throw new RuntimeException("L'email doit contenir un @");
        }

        // Mettre à jour le profil
        compte.setNom(nouveauNom);
        compte.setEmail(nouvelEmail);
        compteRepository.save(compte);
    }
}
