package com.LGS.model.View.SOSContact;

import com.LGS.model.SOSContact.SOSContactPacket;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
public class SOSContactBundleView {

        private String name;

        private SOSContactPacket contactInfo;


        public SOSContactBundleView(String name, SOSContactPacket contactInfo) {
            this.name = name;
            this.contactInfo = contactInfo;
        }
}


