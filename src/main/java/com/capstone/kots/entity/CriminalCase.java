package com.capstone.kots.entity;

import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "criminal_cases")
@ToString
public class CriminalCase implements Serializable {
    private int id;
    private Integer caseId;
    private Case aCase;
    private String criminalCaseImage;
    private String content;
    private Timestamp createdTime;

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
    @Column(name = "case_id")
    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    @Transient
    public Case getaCase() {
        return aCase;
    }

    public void setaCase(Case aCase) {
        this.aCase = aCase;
    }

    @Basic
    @Column(name = "criminal_case_image")
    public String getCriminalCaseImage() {
        return criminalCaseImage;
    }

    public void setCriminalCaseImage(String criminalCaseImage) {
        this.criminalCaseImage = criminalCaseImage;
    }

    @Basic
    @Column(name = "content")
    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    @Basic
    @Column(name = "created_time")
    public Timestamp getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(Timestamp createdTime) {
        this.createdTime = createdTime;
    }
}
