package com.example.notificationmanager.model;

public class PushNotification {
    private NotificationModel data;
    private String to;

    public NotificationModel getData() {
        return data;
    }

    public void setData(NotificationModel data) {
        this.data = data;
    }

    public String getTo() {
        return to;
    }

    public void setTo(String to) {
        this.to = to;
    }

    public PushNotification(NotificationModel data, String to) {
        this.data = data;
        this.to = to;
    }
}
