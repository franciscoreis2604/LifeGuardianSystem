package com.LGS.dao.SOSContact;

import com.LGS.model.SOSContact.SOSContactBundle;
import com.LGS.model.View.SOSContact.SOSContactBundleView;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SOSContactDao {

    boolean insertSOSContact(SOSContactBundle SOSContactBundle);

    Optional<List<SOSContactBundleView>> selectSOSContactByUserId(UUID uId);

    int updateSOSContactBundle(SOSContactBundle sosContactBundle);

    int deleteSOSContactBundle(SOSContactBundle sosContactBundle);


}
