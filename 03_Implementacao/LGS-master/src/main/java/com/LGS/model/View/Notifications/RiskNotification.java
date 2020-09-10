package com.LGS.model.View.Notifications;

public class RiskNotification {

    private String riskDescription;

    public RiskNotification(String riskDescription) {
        this.riskDescription = riskDescription;
    }

    @Override
    public String toString() {
        return "You are at Risk! (" + riskDescription + ")";
    }
}
