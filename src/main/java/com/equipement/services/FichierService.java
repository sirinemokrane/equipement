package com.equipement.services;

import com.equipement.entity.Fichier;
import com.equipement.Repository.FichierRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Date;
import java.util.Objects;

@Service
public class FichierService {

    @Autowired
    private FichierRepository fichierRepository;

    // Chemin de stockage des fichiers
    private final String UPLOAD_DIR = "uploads/";

    // Méthode pour récupérer un fichier par son ID
    public Fichier getFichierById(Long idFichier) {
        return fichierRepository.findById(idFichier)
                .orElseThrow(() -> new RuntimeException("Fichier non trouvé"));
    }

    // Méthode pour uploader un fichier
    public boolean upload(MultipartFile file, Long idInterface, String typeFichier) {
        Objects.requireNonNull(file, "Le fichier ne peut pas être null");
        Objects.requireNonNull(idInterface, "L'ID de l'interface ne peut pas être null");
        Objects.requireNonNull(typeFichier, "Le type de fichier ne peut pas être null");
        try {
            // Créer le répertoire de stockage s'il n'existe pas
            Path uploadPath = Paths.get(UPLOAD_DIR);
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            // Générer un nom unique pour le fichier
            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            // Sauvegarder le fichier sur le disque
            Files.copy(file.getInputStream(), filePath);
            // Créer un objet Fichier et le sauvegarder en base de données
            Fichier fichier = new Fichier();
            fichier.setNom(file.getOriginalFilename());
            fichier.setTypeFichier(typeFichier);
            fichier.setTaille(file.getSize());
            fichier.setDateUpload(new Date());
            fichier.setCheminFichier(filePath.toString());
            // Supposons que vous avez un service ou repository pour InterfaceElectronique
            // fichier.setInterfaceElectronique(interfaceElectroniqueService.getInterfaceById(idInterface));
            fichierRepository.save(fichier);
            return true;
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de l'upload du fichier : " + e.getMessage());
        }
    }

    // Méthode pour télécharger un fichier
    public void telecharger(Long idFichier, Path destination) {
        Objects.requireNonNull(idFichier, "L'ID du fichier ne peut pas être null");
        Objects.requireNonNull(destination, "Le chemin de destination ne peut pas être null");
        Fichier fichier = getFichierById(idFichier);
        try {
            // Copier le fichier vers la destination
            Path source = Paths.get(fichier.getCheminFichier());
            Files.copy(source, destination);
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors du téléchargement du fichier : " + e.getMessage());
        }
    }

    // Méthode pour supprimer un fichier
    public boolean supprimer(Long idFichier) {
        Objects.requireNonNull(idFichier, "L'ID du fichier ne peut pas être null");
        Fichier fichier = getFichierById(idFichier);
        try {
            // Supprimer le fichier du système de fichiers
            Files.deleteIfExists(Paths.get(fichier.getCheminFichier()));
            // Supprimer le fichier de la base de données
            fichierRepository.delete(fichier);
            return true;
        } catch (IOException e) {
            throw new RuntimeException("Erreur lors de la suppression du fichier : " + e.getMessage());
        }
    }
}
