package com.capstone.kots.entity;

import java.util.List;

public class NotificationResponse {
    private int badge;
    private List<CaseNotification> listNotif;

    public NotificationResponse() {
    }

    public int getBadge() {
        return badge;
    }

    public void setBadge(int badge) {
        this.badge = badge;
    }

    public List<CaseNotification> getListNotif() {
        return listNotif;
    }

    public void setListNotif(List<CaseNotification> listNotif) {
        this.listNotif = listNotif;
    }
}
