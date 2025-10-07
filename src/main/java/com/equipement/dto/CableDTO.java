package com.equipement.dto;

import java.util.Date;
import java.util.List;

public class CableDTO {
    private Long idCable;
    private String type;
    private String serialNb;
    private Integer channelNb;
    private String lineName;
    private Double pointNb;
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
    private Date lastTestDate;
    private Long cxMaster;
    private List<Long> fichierIds;

    public CableDTO() {}

    // getters & setters
    public Long getIdCable() { return idCable; }
    public void setIdCable(Long idCable) { this.idCable = idCable; }
    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
    public String getSerialNb() { return serialNb; }
    public void setSerialNb(String serialNb) { this.serialNb = serialNb; }
    public Integer getChannelNb() { return channelNb; }
    public void setChannelNb(Integer channelNb) { this.channelNb = channelNb; }
    public String getLineName() { return lineName; }
    public void setLineName(String lineName) { this.lineName = lineName; }
    public Double getPointNb() { return pointNb; }
    public void setPointNb(Double pointNb) { this.pointNb = pointNb; }
    public String getState() { return state; }
    public void setState(String state) { this.state = state; }
    public String getAutoTest() { return autoTest; }
    public void setAutoTest(String autoTest) { this.autoTest = autoTest; }
    public Long getEasting() { return easting; }
    public void setEasting(Long easting) { this.easting = easting; }
    public Long getNorthing() { return northing; }
    public void setNorthing(Long northing) { this.northing = northing; }
    public Integer getElevation() { return elevation; }
    public void setElevation(Integer elevation) { this.elevation = elevation; }
    public Double getNoise() { return noise; }
    public void setNoise(Double noise) { this.noise = noise; }
    public Double getDistortion() { return distortion; }
    public void setDistortion(Double distortion) { this.distortion = distortion; }
    public Double getPhase() { return phase; }
    public void setPhase(Double phase) { this.phase = phase; }
    public Double getGain() { return gain; }
    public void setGain(Double gain) { this.gain = gain; }
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }
    public Date getLastTestDate() { return lastTestDate; }
    public void setLastTestDate(Date lastTestDate) { this.lastTestDate = lastTestDate; }
    public Long getCxMaster() { return cxMaster; }
    public void setCxMaster(Long cxMaster) { this.cxMaster = cxMaster; }
    public List<Long> getFichierIds() { return fichierIds; }
    public void setFichierIds(List<Long> fichierIds) { this.fichierIds = fichierIds; }
}
