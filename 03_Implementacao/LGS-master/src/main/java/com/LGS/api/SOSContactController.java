package com.LGS.api;

import com.LGS.exception.ApiRequestException;
import com.LGS.model.SOSContact.SOSContactBundle;
import com.LGS.model.View.SOSContact.SOSContactBundleView;
import com.LGS.service.SOSContactNotificationServiceImpl;
import com.LGS.service.SOSContactServiceImpl;
import lombok.NonNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RequestMapping("lgs/v1/user/contacts")
@RestController
public class SOSContactController {

    private final SOSContactServiceImpl sosContactService;
    private SOSContactNotificationServiceImpl sosContactNotificationService;

    @Autowired
    public SOSContactController(SOSContactServiceImpl sosContactService, SOSContactNotificationServiceImpl sosContactNotificationService) {
        this.sosContactService = sosContactService;
        this.sosContactNotificationService = sosContactNotificationService;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public String insertSOSContact(@Valid @NonNull @RequestBody SOSContactBundle sosContactBundle) {
        if (sosContactService.insertSOSContact(sosContactBundle))
            return "Contact inserted";
        throw new ApiRequestException("Contact couldn't be inserted");
    }

    @GetMapping(path = "{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public List<SOSContactBundleView> selectSOSContactByUserId(@PathVariable("id") UUID uId) {
        return sosContactService.selectSOSContactByUserId(uId).
                orElseThrow(() -> new ApiRequestException("User contacts not found"));
    }

    @PutMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public String updateSOSContactBundle(@Valid @NonNull @RequestBody SOSContactBundle sosContactBundle) {
        if (sosContactService.updateSOSContactBundle(sosContactBundle))
            return "SOS contact updated";
        else
            throw new ApiRequestException("SOS contact could not be updated");

    }


    @DeleteMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public String deleteSOSContactBundle(@Valid @NonNull @RequestBody SOSContactBundle sosContactBundle) {
        if (sosContactService.deleteSOSContactBundle(sosContactBundle))
            return "SOS contact deleted";
        else
            throw new ApiRequestException("SOS contact could not be updated");
    }

    @GetMapping(path = "/notify/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public String sendNotificationToSOSContacts(@PathVariable("id") UUID uId) {
        if (sosContactNotificationService.contactSOSContact(uId))
            return "Notified";
        throw new ApiRequestException("Couldn't notify SOS contacts");
    }

}
