package com.LGS.service;

import com.LGS.dao.SOSContact.SOSContactDao;
import com.LGS.model.SOSContact.SOSContactBundle;
import com.LGS.model.View.SOSContact.SOSContactBundleView;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;


@Slf4j
@Service
public class SOSContactServiceImpl implements SOSContactService {

    private SOSContactDao sosContactDao;

    @Autowired
    public SOSContactServiceImpl(@Qualifier("postgres_sosContacts") SOSContactDao sosContactDao){ this.sosContactDao = sosContactDao; }


    @Override
    public boolean insertSOSContact(SOSContactBundle SOSContactBundle) {
        return sosContactDao.insertSOSContact(SOSContactBundle);
    }

    @Override
    public Optional<List<SOSContactBundleView>> selectSOSContactByUserId(UUID uId) {
        return sosContactDao.selectSOSContactByUserId(uId);
    }


    @Override
    public boolean updateSOSContactBundle(SOSContactBundle sosContactBundle) {
        int value =  sosContactDao.updateSOSContactBundle(sosContactBundle);
        return value > 0;
    }

    @Override
    public boolean deleteSOSContactBundle(SOSContactBundle sosContactBundle) {
        int value = sosContactDao.deleteSOSContactBundle(sosContactBundle);
        return value > 0;
    }
}
