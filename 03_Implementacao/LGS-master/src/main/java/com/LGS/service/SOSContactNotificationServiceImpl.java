package com.LGS.service;

import com.LGS.config.Twilio.TwilioConfig;
import com.LGS.dao.SOSContact.SOSContactDao;
import com.LGS.dao.User.UserDao;
import com.LGS.model.SOSContact.SOSContactPacket;
import com.LGS.model.View.Notifications.SOSNotification;
import com.LGS.model.View.SOSContact.SOSContactBundleView;
import com.LGS.model.View.User.UserView;
import com.twilio.rest.api.v2010.account.Message;
import com.twilio.type.PhoneNumber;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class SOSContactNotificationServiceImpl implements SOSContactNotificationService {

    private JavaMailSender javaMailSender;
    private SOSContactDao sosContactDao;
    private final TwilioConfig twilioConfig;
    private UserDao userDao;


    @Autowired
    public SOSContactNotificationServiceImpl(@Qualifier("postgres_sosContacts") SOSContactDao sosContactDao,
                                             @Qualifier("postgres") UserDao userDao,
                                             TwilioConfig twilioConfig,
                                             JavaMailSender javaMailSender) {
        this.sosContactDao = sosContactDao;
        this.javaMailSender = javaMailSender;
        this.twilioConfig = twilioConfig;
        this.userDao = userDao;
    }

    @Override
    public boolean contactSOSContact(UUID userID) {
        Optional<List<SOSContactBundleView>> sosContacts = sosContactDao.selectSOSContactByUserId(userID);
        if (sosContacts.isPresent()) {
            for (SOSContactBundleView sosContactBundleView : sosContacts.get()) {
                SOSContactPacket sosContactPacket = sosContactBundleView.getContactInfo();
                UserView user = userDao.selectUserViewById(userID).get();
                SOSNotification SOSNotification = new SOSNotification(sosContactBundleView.getName(), user.getName());
                if (sosContactPacket.getContactType().equals("Email")) {
                    SimpleMailMessage mail = new SimpleMailMessage();
                    mail.setTo(sosContactPacket.getContactValue());
                    mail.setFrom("noreplay@lgs.com");
                    mail.setSubject("SOS Alert");
                    mail.setText(SOSNotification.toString());
                    javaMailSender.send(mail);
                }
                if (sosContactPacket.getContactType().equals("Mobile")) {
                    PhoneNumber to = new PhoneNumber(sosContactPacket.getContactValue());
                    PhoneNumber from = new PhoneNumber(twilioConfig.getTrailNumber());
                    String body = SOSNotification.toString();
                    Message.creator(to, from, body).create();
                }
            }
            return true;
        } else {
            return false;
        }
    }
}
