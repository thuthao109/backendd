package com.capstone.kots.entity;

import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "trace_points")
@ToString
public class TracePoint implements Serializable {
    private int id;
    private Integer caseId;
    private Case aCase;
    private String tracePointName;
    private String description;


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
    @Column(name = "trace_point_name")
    public String getTracePointName() {
        return tracePointName;
    }

    public void setTracePointName(String tracePointName) {
        this.tracePointName = tracePointName;
    }

    @Basic
    @Column(name = "description")
    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
