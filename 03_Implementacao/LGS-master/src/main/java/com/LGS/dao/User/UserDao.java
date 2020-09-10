package com.LGS.dao.User;

import com.LGS.model.User.User;
import com.LGS.model.User.UserCredentials;
import com.LGS.model.View.User.UserView;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Operations allowed for Users
 */
public interface UserDao {

    /**
     * UserView user to db with a given ID
     * @param id - userId
     * @param user - UserView
     * @return value
     */
    UUID insertUser(UUID id, User user);


    /**
     * Insert a user to db with a generated ID
     * @param user - UserView
     * @return value
     */
    default UUID insertUser(User user){
        UUID id = UUID.randomUUID();
        return insertUser(id,user);
    }

    /**
     * Returns all users in DB
     * @return value
     */
    List<UserView> selectAllUsers();


    /**
     * Returns a user with given id
     * @param id - userId
     * @return value
     */
    Optional<UserView> selectUserViewById(UUID id);

    /**
     * Delete a user with given ID from the DB
     * @param id - userId
     * @return value
     */
    boolean deleteUserById(UUID id);

    /**
     * Updates info from a userView with given ID
     * @param id - userId
     * @param userView - UserView
     * @return value
     */

    int updateUserById(UUID id, UserView userView);

    /*
    NEW THINGS
     */

    /**
     * Updates login information
     *
     * @param id      userId
     * @param newUser new user credentials
     * @return 0 if failed, 1 if updated
     */

    int updateLoginUserById(UUID id, UserCredentials newUser);

    /**
     * Get login credentials for user
     *
     * @param id userID
     * @return Optional of user credentials
     */
    Optional<UserCredentials> selectLoginCredentialsById(UUID id);

    /**
     * Load user by username
     *
     * @param username username
     * @return optional of User
     */
    Optional<User> loadUserByUsername(String username);

    /**
     * Get userId by credentials
     *
     * @param username username
     * @return userID
     */
    UUID getUserIdByCredentials(String username);

    Optional<UserView> verifyUserUUID(UUID userId);


}
