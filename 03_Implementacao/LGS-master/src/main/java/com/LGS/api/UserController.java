package com.LGS.api;

import com.LGS.exception.ApiRequestException;
import com.LGS.model.User.User;
import com.LGS.model.User.UserCredentials;
import com.LGS.model.View.User.UserView;
import com.LGS.service.UserServiceImpl;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.lang.NonNull;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.UUID;

@RequestMapping("lgs/v1/user")
@RestController
public class UserController {

    private final UserServiceImpl userServiceImpl;

    @Autowired
    public UserController(UserServiceImpl userServiceImpl) {
        this.userServiceImpl = userServiceImpl;
    }

    @PostMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public UUID insertUser(@Valid @NonNull @RequestBody User user){
        return userServiceImpl.insertUser(user);
    }

    @GetMapping
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public List<UserView> selectAllUsers(){
        return userServiceImpl.selectAllUsers();
    }

    @GetMapping(path = "{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public UserView selectUserById(@PathVariable("id") UUID id){
        return userServiceImpl.selectUserById(id)
                .orElseThrow(() -> new ApiRequestException("User not found"));
    }

    @DeleteMapping(path = "{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String deleteUserById(@PathVariable("id") UUID id) {

        if (userServiceImpl.deleteUserById(id))
            return "User deleted";
        else
            throw new ApiRequestException("User could not be deleted");
    }

    @PutMapping(path = "{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    public String updateUserById(@PathVariable("id") UUID id, @Valid @NonNull @RequestBody UserView userViewToUpdate) {
        if (userServiceImpl.updateUserById(id, userViewToUpdate) == 1)
            return "User updated";
        else
            throw new ApiRequestException("User could not be updated");
    }


    /*
    NEW THINGS
     */


    /* GETS USER CREDENTIALS.
    @GetMapping(path = "credentials/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public UserCredentials selectLoginCredentialsById(@PathVariable("id") UUID id){
        return userServiceImpl.selectLoginCredentialsById(id)
                .orElseThrow(() -> new ApiRequestException("UserView not found"));
    }
     */

    @PutMapping(path = "credentials/{id}")
    @PreAuthorize("hasAnyRole('ROLE_ADMIN','ROLE_USER')")
    public String updateLoginUserById(@PathVariable("id") UUID id, @Valid @NonNull @RequestBody UserCredentials userToUpdate) {
        if (userServiceImpl.updateLoginUserById(id, userToUpdate) == 1)
            return "Credentials updated";
        else
            throw new ApiRequestException("Credentials could not be deleted");
    }



}
