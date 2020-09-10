package com.LGS.model.Risk;

import lombok.Getter;

public enum Risk {
    // 2 packets/second for all of them
    HIGH_RISK("High Risk", 60),
    MEDIUM_RISK("Medium Risk", 720),
    LOW_RISK("Low Risk", 2880);

    @Getter
    private String riskDescription;
    @Getter
    private int minimumNumberOfPackets;

    Risk(String riskDescription, int minimumNumberOfPackets) {
        this.riskDescription = riskDescription;
        this.minimumNumberOfPackets = minimumNumberOfPackets;
    }
}
