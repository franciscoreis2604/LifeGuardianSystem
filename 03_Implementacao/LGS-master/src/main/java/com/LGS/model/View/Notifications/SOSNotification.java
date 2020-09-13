package com.LGS.model.View.Notifications;

public class SOSNotification {
    private String username;
    private String contactName;

    public SOSNotification(String contactName, String username) {
        this.username = username;
        this.contactName = contactName;
    }

    @Override
    public String toString() {
        return "Hey " + contactName + "! \n" +
                "Your friend " + username + " might be in danger, contact them!";
    }
}
