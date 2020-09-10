package com.LGS.service;

import com.LGS.model.SOSContact.SOSContactBundle;
import com.LGS.model.View.SOSContact.SOSContactBundleView;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SOSContactService {

    boolean insertSOSContact(SOSContactBundle SOSContactBundle);

    Optional<List<SOSContactBundleView>> selectSOSContactByUserId(UUID uId);

    boolean updateSOSContactBundle(SOSContactBundle sosContactBundle);

    boolean deleteSOSContactBundle(SOSContactBundle sosContactBundle);
}
