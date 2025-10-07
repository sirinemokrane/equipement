-- Script SQL pour enrichir la base de données equipementt
-- Ordre d'insertion respectant les contraintes de clés étrangères

-- =============================================================================
-- 1. INSERTION DES STRUCTURES (table indépendante)
-- =============================================================================

INSERT INTO structure (nom, localisation, type_structure) VALUES 
('Centre Technique Principal', 'Paris, France', 'CENTRE_TECHNIQUE'),
('Département Électronique', 'Lyon, France', 'DEPARTEMENT'),
('Laboratoire R&D', 'Toulouse, France', 'LABORATOIRE'),
('Service Maintenance', 'Marseille, France', 'SERVICE'),
('Bureau d''Études', 'Bordeaux, France', 'BUREAU_ETUDES');

-- =============================================================================
-- 2. INSERTION DES COMPTES (dépendent des structures)
-- =============================================================================

INSERT INTO compte (nom, email, mot_de_passe, type_compte, date_creation, id_structure) VALUES 
('Jean Dupont', 'jean.dupont@entreprise.com', 'motdepasse123', 'GESTIONNAIRE', '2025-10-03', 1),
('Marie Martin', 'marie.martin@entreprise.com', 'password456', 'TECHNICIEN', '2025-10-03', 1),
('Pierre Durand', 'pierre.durand@entreprise.com', 'secret789', 'TECHNICIEN', '2025-10-03', 2),
('Sophie Leroy', 'sophie.leroy@entreprise.com', 'pass2025', 'GESTIONNAIRE', '2025-10-03', 2),
('Antoine Moreau', 'antoine.moreau@entreprise.com', 'tech123', 'TECHNICIEN', '2025-10-03', 3),
('Isabelle Bernard', 'isabelle.bernard@entreprise.com', 'secure456', 'GESTIONNAIRE', '2025-10-03', 3),
('Nicolas Petit', 'nicolas.petit@entreprise.com', 'admin789', 'TECHNICIEN', '2025-10-03', 4),
('Caroline Roux', 'caroline.roux@entreprise.com', 'manage2025', 'GESTIONNAIRE', '2025-10-03', 5);

-- =============================================================================
-- 3. INSERTION DES CÂBLES (table indépendante)
-- =============================================================================

INSERT INTO cable (num_serie, etat) VALUES 
('CBL-2025-001', true),
('CBL-2025-002', false),
('CBL-HDMI-003', true),
('CBL-USB-004', true),
('CBL-ETHERNET-005', true),
('CBL-POWER-006', false),
('CBL-AUDIO-007', true),
('CBL-VIDEO-008', true),
('CBL-DATA-009', false),
('CBL-FIBER-010', true);

-- =============================================================================
-- 4. INSERTION DES INTERFACES ÉLECTRONIQUES (dépendent des comptes)
-- =============================================================================

INSERT INTO interface_electronique (nom, date_creation, observations, id_compte) VALUES 
('Interface Audio Premium', '2025-10-03 10:00:00', 'Interface haute qualité pour traitement audio', 1),
('Interface Vidéo 4K', '2025-10-03 11:00:00', 'Supporte la résolution 4K et HDR', 2),
('Interface Réseau Gigabit', '2025-10-03 12:00:00', 'Interface Ethernet haute vitesse', 3),
('Interface USB 3.0 Hub', '2025-10-03 13:00:00', 'Hub USB multi-ports haute vitesse', 4),
('Interface HDMI Splitter', '2025-10-03 14:00:00', 'Répartiteur HDMI 1 vers 4', 5),
('Interface Convertisseur A/D', '2025-10-03 15:00:00', 'Convertisseur analogique vers numérique', 6),
('Interface Amplificateur', '2025-10-03 16:00:00', 'Amplificateur de signal audio', 7),
('Interface Commutateur KVM', '2025-10-03 17:00:00', 'Commutateur clavier-vidéo-souris', 8);

-- =============================================================================
-- 5. INSERTION DES TYPE_COMPTE (si table séparée - optionnel car c'est un enum)
-- =============================================================================
-- Cette section n'est nécessaire que si TypeCompte est une table et non un enum
-- INSERT INTO type_compte (nom, description) VALUES 
-- ('GESTIONNAIRE', 'Responsable de la gestion des équipements'),
-- ('TECHNICIEN', 'Technicien spécialisé en maintenance');

-- =============================================================================
-- REQUÊTES DE VÉRIFICATION
-- =============================================================================

-- Vérifier les données insérées
SELECT 'STRUCTURES' as table_name, COUNT(*) as nb_records FROM structure
UNION ALL
SELECT 'COMPTES' as table_name, COUNT(*) as nb_records FROM compte
UNION ALL
SELECT 'CABLES' as table_name, COUNT(*) as nb_records FROM cable
UNION ALL
SELECT 'INTERFACES' as table_name, COUNT(*) as nb_records FROM interface_electronique;

-- Afficher un résumé des données par structure
SELECT 
    s.nom as structure_name,
    COUNT(c.id_compte) as nb_comptes,
    STRING_AGG(c.nom, ', ') as noms_comptes
FROM structure s
LEFT JOIN compte c ON s.id_structure = c.id_structure
GROUP BY s.id_structure, s.nom
ORDER BY s.id_structure;

-- Afficher les interfaces par compte
SELECT 
    c.nom as compte_name,
    c.type_compte,
    COUNT(i.id_interface) as nb_interfaces,
    STRING_AGG(i.nom, ', ') as noms_interfaces
FROM compte c
LEFT JOIN interface_electronique i ON c.id_compte = i.id_compte
GROUP BY c.id_compte, c.nom, c.type_compte
ORDER BY c.id_compte;



commit ;