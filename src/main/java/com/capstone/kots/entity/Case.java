package com.capstone.kots.entity;

import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "cases")
@ToString
public class Case implements Serializable {

    private int id;
    private Integer createdId;
    private Integer confirmedId;
    private Timestamp createdTime;
    private Timestamp deletedTime;
    private String deletedReason;
    private String caseCode;
    private double latitude;
    private double longitude;
    private String address;
    private String caseTag;
    private int caseTagType;
    private int displayType;
    private String caseSource;
    private int peopleLimit;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    @Basic
    @Column(name = "created_id")
    public Integer getCreatedId() {
        return createdId;
    }

    public void setCreatedId(Integer createdId) {
        this.createdId = createdId;
    }

    @Basic
    @Column(name = "confirmed_id")
    public Integer getConfirmedId() {
        return confirmedId;
    }

    public void setConfirmedId(Integer confirmedId) {
        this.confirmedId = confirmedId;
    }


    @Basic
    @Column(name = "created_time")
    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }


    @Basic
    @Column(name = "deleted_time")
    public Timestamp getDeletedTime() {
        return deletedTime;
    }

    public void setDeletedTime(Timestamp deletedTime) {
        this.deletedTime = deletedTime;
    }

    @Basic
    @Column(name = "deleted_reason")
    public String getDeletedReason() {
        return deletedReason;
    }

    public void setDeletedReason(String deletedReason) {
        this.deletedReason = deletedReason;
    }

    @Basic
    @Column(name = "case_code")
    public String getCaseCode() {
        return caseCode;
    }

    public void setCaseCode(String caseCode) {
        this.caseCode = caseCode;
    }

    @Basic
    @Column(name = "latitude")
    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Basic
    @Column(name = "longitude")
    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Basic
    @Column(name = "address")
    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Basic
    @Column(name = "case_tag")
    public String getCaseTag() {
        return caseTag;
    }

    public void setCaseTag(String caseTag) {
        this.caseTag = caseTag;
    }

    @Basic
    @Column(name = "case_tag_type")
    public int getCaseTagType() {
        return caseTagType;
    }

    public void setCaseTagType(int caseTagType) {
        this.caseTagType = caseTagType;
    }

    @Basic
    @Column(name = "display_type")
    public int getDisplayType() {
        return displayType;
    }

    public void setDisplayType(int displayType) {
        this.displayType = displayType;
    }


    @Basic
    @Column(name = "case_source")
    public String getCaseSource() {
        return caseSource;
    }

    public void setCaseSource(String caseSource) {
        this.caseSource = caseSource;
    }

    @Basic
    @Column(name = "people_limit")
    public int getPeopleLimit() {
        return peopleLimit;
    }

    public void setPeopleLimit(int peopleLimit) {
        this.peopleLimit = peopleLimit;
    }
}
