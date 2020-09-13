package com.LGS.service;


import com.LGS.model.User.User;
import com.LGS.model.User.UserCredentials;
import com.LGS.model.View.User.UserView;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public interface UserService {


    UUID insertUser(User user);

    List<UserView> selectAllUsers();

    Optional<UserView> selectUserById(UUID id);

    boolean deleteUserById(UUID id);

    int updateUserById(UUID id, UserView newUserView);

    int updateLoginUserById(UUID id, UserCredentials newUser);

    Optional<UserCredentials> selectLoginCredentialsById(UUID id);

    UUID getUserIdByCredentials(String username);
}
