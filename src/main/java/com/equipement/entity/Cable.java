package com.equipement.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;
@Data
@Entity
@Table(name = "Cable")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class Cable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long idCable;

    private String type;
    private String serialNb;
    private Integer channelNb;
    private String lineName;
    private Double pointNb;
    @Column(name = "etat", nullable = false)
    // map Java field 'state' to legacy DB column 'etat' (not-null in DB)
    private String state;
    private String autoTest;
    private Long easting;
    private Long northing;
    private Integer elevation;
    private Double noise;
    private Double distortion;
    private Double phase;
    private Double gain;
    private String version;
    @Temporal(TemporalType.TIMESTAMP)
    private Date lastTestDate;
    private Long cxMaster;

    @ManyToMany(mappedBy = "cables")
    private List<Fichier> fichiers;
}
