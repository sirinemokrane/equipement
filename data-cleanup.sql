-- Script SQL pour nettoyer la base de données avant insertion
-- ATTENTION : Ce script supprime TOUTES les données existantes !

-- =============================================================================
-- NETTOYAGE DES TABLES (dans l'ordre inverse des dépendances)
-- =============================================================================

-- Désactiver temporairement les contraintes de clés étrangères
SET session_replication_role = replica;

-- Vider les tables dans l'ordre des dépendances (du plus dépendant au moins dépendant)
TRUNCATE TABLE fichier CASCADE;
TRUNCATE TABLE interface_electronique CASCADE;
TRUNCATE TABLE cable CASCADE;
TRUNCATE TABLE compte CASCADE;
TRUNCATE TABLE structure CASCADE;

-- Réactiver les contraintes de clés étrangères
SET session_replication_role = DEFAULT;

-- Réinitialiser les séquences des ID auto-générés
ALTER SEQUENCE IF EXISTS structure_id_structure_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS compte_id_compte_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS cable_id_cable_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS interface_electronique_id_interface_seq RESTART WITH 1;
ALTER SEQUENCE IF EXISTS fichier_id_fichier_seq RESTART WITH 1;

-- Vérification que les tables sont vides
SELECT 'STRUCTURES' as table_name, COUNT(*) as nb_records FROM structure
UNION ALL
SELECT 'COMPTES' as table_name, COUNT(*) as nb_records FROM compte
UNION ALL
SELECT 'CABLES' as table_name, COUNT(*) as nb_records FROM cable
UNION ALL
SELECT 'INTERFACES' as table_name, COUNT(*) as nb_records FROM interface_electronique
UNION ALL
SELECT 'FICHIERS' as table_name, COUNT(*) as nb_records FROM fichier;
