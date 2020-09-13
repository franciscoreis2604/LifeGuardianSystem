package com.LGS.model.Caution;

import lombok.Getter;

public enum Caution {

    // 2 packets/second for all of them
    HIGH_CAUTION("High caution", 60),
    MEDIUM_CAUTION("Medium caution", 720),
    LOW_CAUTION("Low caution", 2880);

    @Getter
    private String cautionDescription;
    @Getter
    private int minimumNumberOfPackets;

    Caution(String cautionDescription, int minimumNumberOfPackets) {
        this.cautionDescription = cautionDescription;
        this.minimumNumberOfPackets = minimumNumberOfPackets;
    }
}

