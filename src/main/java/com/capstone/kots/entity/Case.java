package com.capstone.kots.entity;

import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.List;
import java.util.Set;

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
    private Integer deletedUserId;
    private String caseDescription;
    //---
    // 1 occuring
    // 2 done
    // 3 cancel
    // 4 pending
    private Integer caseStatus;
    //---

    private User createdUser;

    private String caseName;

    @Transient
    public User getCreatedUser() {
        return createdUser;
    }

    public void setCreatedUser(User createdUser) {
        this.createdUser = createdUser;
    }

    @OneToMany(mappedBy = "cases")
    List<UserJoinCase> userJoinCases;

    @Transient
    public List<UserJoinCase> getUserJoinCases() {
        return userJoinCases;
    }

    public void setUserJoinCases(List<UserJoinCase> userJoinCases) {
        this.userJoinCases = userJoinCases;
    }

    @Basic
    @Column(name = "deleted_user_id")
    public Integer getDeletedUserId() {
        return deletedUserId;
    }

    public void setDeletedUserId(Integer deletedUserId) {
        this.deletedUserId = deletedUserId;
    }

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
    @Column(name = "case_code")
    public String getCaseCode() {
        return caseCode;
    }

    public void setCaseCode(String caseCode) {
        this.caseCode = caseCode;
    }

    @Basic
    @Column(name = "latitude")
    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Basic
    @Column(name = "longitude")
    public Double getLongitude() {
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
    public Integer getCaseTagType() {
        return caseTagType;
    }

    public void setCaseTagType(int caseTagType) {
        this.caseTagType = caseTagType;
    }

    @Basic
    @Column(name = "display_type")
    public Integer getDisplayType() {
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
    public Integer getPeopleLimit() {
        return peopleLimit;
    }

    public void setPeopleLimit(int peopleLimit) {
        this.peopleLimit = peopleLimit;
    }

    @Basic
    @Column(name = "case_name")
    public String getCaseName() {
        return caseName;
    }

    public void setCaseName(String caseName) {
        this.caseName = caseName;
    }

    @Basic
    @Column(name = "case_description")
    public String getCaseDescription() {
        return caseDescription;
    }

    public void setCaseDescription(String caseDescription) {
        this.caseDescription = caseDescription;
    }

    @Basic
    @Column(name = "case_status")
    public Integer getCaseStatus() {
        return caseStatus;
    }

    public void setCaseStatus(Integer caseStatus) {
        this.caseStatus = caseStatus;
    }
}
