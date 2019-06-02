package com.capstone.kots.entity;

import lombok.ToString;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Entity
@Table(name = "user_join_cases")
@ToString
public class UserJoinCase implements Serializable {

    private int id;
    private Integer userId;
    private User user;
    private Integer caseId;
    private Case oneCase;
    private Timestamp joinedTime;

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
    @Column(name = "user_id")
    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    @Basic
    @Column(name = "case_id")
    public Integer getCaseId() {
        return caseId;
    }

    public void setCaseId(Integer caseId) {
        this.caseId = caseId;
    }

    @Basic
    @Column(name = "joined_time")
    public Timestamp getJoinedTime() {
        return joinedTime;
    }

    public void setJoinedTime(Timestamp joinedTime) {
        this.joinedTime = joinedTime;
    }

    @Transient
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Transient
    public Case getOneCase() {
        return oneCase;
    }

    public void setOneCase(Case oneCase) {
        this.oneCase = oneCase;
    }
}
