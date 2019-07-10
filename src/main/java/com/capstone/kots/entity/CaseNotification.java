package com.capstone.kots.entity;

import lombok.ToString;

import javax.persistence.*;
import java.sql.Timestamp;

@Entity
@Table(name = "case_notification")
@ToString
public class CaseNotification {

    private int id;
    private Boolean read;
    private Timestamp notifiedTime;
    private int notificationType;
    private int notificationCaseSourceId;
    private Case caseOne;
    private int userId;
    private User user;

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
    @Column(name = "is_read")
    public Boolean getRead() {
        return read;
    }

    public void setRead(Boolean read) {
        this.read = read;
    }

    @Basic
    @Column(name = "notified_time")
    public Timestamp getNotifiedTime() {
        return notifiedTime;
    }

    public void setNotifiedTime(Timestamp notifiedTime) {
        this.notifiedTime = notifiedTime;
    }


    @Basic
    @Column(name = "notification_type")
    public int getNotificationType() {
        return notificationType;
    }

    public void setNotificationType(int notificationType) {
        this.notificationType = notificationType;
    }

    @Basic
    @Column(name = "notification_case_source_id")
    public int getNotificationCaseSourceId() {
        return notificationCaseSourceId;
    }

    public void setNotificationCaseSourceId(int notificationCaseSourceId) {
        this.notificationCaseSourceId = notificationCaseSourceId;
    }

    @Basic
    @Column(name = "user_id")
    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    @Transient
    public Case getCaseOne() {
        return caseOne;
    }

    public void setCaseOne(Case caseOne) {
        this.caseOne = caseOne;
    }

    @Transient
    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
