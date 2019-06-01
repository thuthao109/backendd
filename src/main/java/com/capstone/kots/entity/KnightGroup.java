package com.capstone.kots.entity;

import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "knight_groups")
@ToString
public class KnightGroup {
    private int id;
    private Integer groupLeadUserId;
    private User user;
    private String groupName;
    private String desciption;

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
    @Column(name = "group_lead_user_id")
    public Integer getGroupLeadUserId() {
        return groupLeadUserId;
    }

    public void setGroupLeadUserId(Integer groupLeadUserId) {
        this.groupLeadUserId = groupLeadUserId;
    }

    @Transient
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Basic
    @Column(name = "group_name")
    public String getGroupName() {
        return groupName;
    }

    public void setGroupName(String groupName) {
        this.groupName = groupName;
    }

    @Basic
    @Column(name = "description")
    public String getDesciption() {
        return desciption;
    }

    public void setDesciption(String desciption) {
        this.desciption = desciption;
    }
}
