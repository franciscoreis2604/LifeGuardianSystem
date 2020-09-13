package com.LGS.service;

import com.LGS.dao.User.UserDao;
import com.LGS.model.User.User;
import com.LGS.model.User.UserCredentials;
import com.LGS.model.View.User.UserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserServiceImpl implements UserService, UserDetailsService {

    private final UserDao userDao;

    @Autowired
    public UserServiceImpl(@Qualifier("postgres") UserDao userDao){
        this.userDao = userDao;
    }

    public UUID insertUser(User user){
        return userDao.insertUser(user);
    }

    public List<UserView> selectAllUsers(){
        return userDao.selectAllUsers();
    }

    public Optional<UserView> selectUserById(UUID id) {
        return userDao.selectUserViewById(id);
    }

    public boolean deleteUserById(UUID id) {
        return userDao.deleteUserById(id);
    }

    public int updateUserById(UUID id, UserView newUserView) {
        return userDao.updateUserById(id, newUserView);
    }

    public int updateLoginUserById(UUID id, UserCredentials newUser) {
        return userDao.updateLoginUserById(id, newUser);
    }

    @Override
    public Optional<UserCredentials> selectLoginCredentialsById(UUID id) {
        return userDao.selectLoginCredentialsById(id);
    }

    @Override
    public UUID getUserIdByCredentials(String username) {
        return userDao.getUserIdByCredentials(username);
    }


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userDao.loadUserByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException(String.format("Username %s not found", username)));
    }
}
