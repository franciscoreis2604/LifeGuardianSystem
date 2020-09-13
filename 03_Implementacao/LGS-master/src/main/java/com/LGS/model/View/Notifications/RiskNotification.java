package com.LGS.model.View.Notifications;

public class RiskNotification {

    private String cautionDescription;

    public RiskNotification(String riskDescription) {
        this.cautionDescription = riskDescription;
    }

    @Override
    public String toString() {
        return "System advices to exert caution. Deviation of standard levels imply: " + cautionDescription;
    }
}
