package com.equipement.batch;

import com.equipement.entity.Cable;
import org.springframework.batch.item.file.mapping.FieldSetMapper;
import org.springframework.batch.item.file.transform.FieldSet;
import org.springframework.validation.BindException;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
// Cette classe implémente FieldSetMapper<Cable> pour mapper les données d'un fichier plat vers un objet Cable.
// Méthode principale de l'interface FieldSetMapper.
// Elle prend en entrée un FieldSet (ensemble de champs lus depuis un fichier) et retourne un objet Cable.
public class CableFieldSetMapper implements FieldSetMapper<Cable> {

    @Override // Cette méthode est responsable de la transformation des données brutes lues depuis un fichier plat (CSV, TXT, etc.)
 // en un objet métier {@link Cable} utilisable par le processus Spring Batch.

    public Cable mapFieldSet(FieldSet fieldSet) throws BindException {
        Cable cable = new Cable();
// Mapping des champs du FieldSet vers les attributs de l'objet Cable.
        // Chaque méthode readXxx lit et convertit le champ correspondant
        try {
            // Mapping des champs
            cable.setType(readString(fieldSet, "type"));
            cable.setSerialNb(readString(fieldSet, "serialNb"));
            cable.setChannelNb(readInteger(fieldSet, "channelNb"));
            cable.setLineName(readString(fieldSet, "lineName"));
            cable.setPointNb(readDouble(fieldSet, "pointNb"));
            cable.setState(readString(fieldSet, "state"));
            cable.setAutoTest(readString(fieldSet, "autoTest"));
            cable.setEasting(readLong(fieldSet, "easting"));
            cable.setNorthing(readLong(fieldSet, "northing"));
            cable.setElevation(readInteger(fieldSet, "elevation"));
            cable.setNoise(readDouble(fieldSet, "noise"));
            cable.setDistortion(readDouble(fieldSet, "distortion"));
            cable.setPhase(readDouble(fieldSet, "phase"));
            cable.setGain(readDouble(fieldSet, "gain"));
            cable.setVersion(readString(fieldSet, "version"));
            cable.setLastTestDate(readDate(fieldSet, "lastTestDate"));
            cable.setCxMaster(readLong(fieldSet, "cxMaster"));

            // Valeur par défaut pour state si null
            if (cable.getState() == null || cable.getState().trim().isEmpty()) {
                cable.setState("ko");
            }

        } catch (Exception e) {
            System.err.println("Error mapping fieldSet: " + e.getMessage());
            throw new BindException(cable, "cable");
        }

        return cable;
    }

    private String readString(FieldSet fs, String name) {
        try {
            String value = fs.readString(name);
            if (value == null || value.trim().isEmpty()) {
                return null;
            }
            return value.trim();
        } catch (Exception e) {
            return null;
        }
    }
    // Méthode utilitaire pour lire un champ de type Integer.
    // Retourne null si le champ est vide ou invalide.
    private Integer readInteger(FieldSet fs, String name) {
        try {
            String value = fs.readString(name);
            if (value == null || value.trim().isEmpty()) {
                return null;
            }
            return Integer.parseInt(value.trim());
        } catch (Exception e) {
            return null;
        }
    }

    private Long readLong(FieldSet fs, String name) {
        try {
            String value = fs.readString(name);
            if (value == null || value.trim().isEmpty()) {
                return null;
            }
            return Long.parseLong(value.trim());
        } catch (Exception e) {
            return null;
        }
    }

    private Double readDouble(FieldSet fs, String name) {
        try {
            String value = fs.readString(name);
            if (value == null || value.trim().isEmpty()) {
                return null;
            }
            return Double.parseDouble(value.trim());
        } catch (Exception e) {
            return null;
        }
    }

    private Date readDate(FieldSet fs, String name) {
        try {
            String value = fs.readString(name);
            if (value == null || value.trim().isEmpty()) {
                return null;
            }

            // Nettoyer la valeur
            String cleanValue = value.trim();

            // Enlever le timezone si présent
            cleanValue = cleanValue.replaceAll(" UTC.*$", "");

            // Essayer différents formats
            SimpleDateFormat[] formats = {
                    new SimpleDateFormat("dd/MM/yyyy HH:mm:ss"),
                    new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss"),
                    new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"),
                    new SimpleDateFormat("yyyy-MM-dd")
            };

            for (SimpleDateFormat format : formats) {
                try {
                    return format.parse(cleanValue);
                } catch (ParseException ignored) {
                    // Essayer le format suivant
                }
            }

            System.err.println("Unable to parse date: " + value);
            return null;

        } catch (Exception e) {
            return null;
        }
    }
}