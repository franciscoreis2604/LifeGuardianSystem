package com.LGS.dao.User;

import com.LGS.exception.ApiRequestException;
import com.LGS.model.User.User;
import com.LGS.model.User.UserCredentials;
import com.LGS.model.View.User.UserView;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository("postgres")
public class UserDataAccessService implements UserDao {

    private final JdbcTemplate jdbcTemplate;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserDataAccessService(JdbcTemplate jdbcTemplate, PasswordEncoder passwordEncoder) {
        this.jdbcTemplate = jdbcTemplate;
        this.passwordEncoder = passwordEncoder;
    }


    @Override
    public UUID insertUser(UUID id, User user) {
        String sql = "with ins_1 as (" +
                "  INSERT INTO users (uid, uname, uphone, ubirthday, urole) VALUES (?,?,?,?,2)" +
                "  returning uid AS user_id)" +
                "INSERT INTO login (lid, lusername, lpassword)" +
                "SELECT user_id, ?,? FROM ins_1";
        try {
        int insertUsersTableAndLogin = jdbcTemplate.update(
                sql,
                id,
                user.getName(),
                user.getPhoneNumber(),
                user.getBirthDate(),
                user.getUsername(),
                passwordEncoder.encode(user.getPassword()));
            if(insertUsersTableAndLogin == 1)  return id;
            throw new ApiRequestException("User could not be created");
        }
        catch (Exception e){
            throw new ApiRequestException("Parameter in data already exists (Email or Phone)");
        }
    }


    @Override
    public List<UserView> selectAllUsers() {
        final String sql = "SELECT * FROM users";
        return jdbcTemplate.query(sql, (resultSet, i) -> {
            String name = resultSet.getString("uname");
            String phone = resultSet.getString("uphone");
            Date birthDate = resultSet.getDate("ubirthday");
            return new UserView(name, phone, birthDate);
        });
    }

    @Override
    public Optional<UserView> selectUserViewById(UUID id) {
        final String sql = "SELECT * FROM users WHERE uid = ?";
        try{
        UserView userView = jdbcTemplate.queryForObject(sql, new Object[]{id},
                (resultSet, i) -> {
                    String name = resultSet.getString("uname");
                    String phone = resultSet.getString("uphone");
                    Date birthDate = resultSet.getDate("ubirthday");
                    return new UserView(name, phone, birthDate);
                });
            return Optional.ofNullable(userView);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public boolean deleteUserById(UUID id) {
        String sqlLogin = "DELETE FROM login " +
                "WHERE lid = ?";
        int deleteFromLogin = jdbcTemplate.update(sqlLogin, id);

        String sqlUsers = "DELETE FROM users " +
                "WHERE uid = ?";
        int deleteFromUsers = jdbcTemplate.update(sqlUsers, id);
        return deleteFromUsers == 1 && deleteFromLogin == 1;
    }

    @Override
    public int updateUserById(UUID id, UserView userView) {
        String sql = "" +
                "UPDATE users " +
                "SET uname = ? , uphone = ? , ubirthday = ?" +
                "WHERE uid = ?";
        return jdbcTemplate.update(sql, userView.getName(), userView.getPhoneNumber(), userView.getBirthDate(), id);
    }


    /*
    NEW THINGS
     */
    @Override
    public int updateLoginUserById(UUID id, UserCredentials newUser){
        String sql = "" +
                "UPDATE login " +
                "SET lusername = ? , lpassword = ?" +
                "WHERE lid = ?";
        return jdbcTemplate.update(sql, newUser.getEmail(),passwordEncoder.encode(newUser.getPassword()),id);
    }


    @Override
    public Optional<UserCredentials> selectLoginCredentialsById(UUID id) {
        final String sql = "SELECT * FROM login WHERE lid = ?";
        try{
            UserCredentials user = jdbcTemplate.queryForObject(sql, new Object[]{id},
                    (resultSet, i) -> {
                        UUID uid = UUID.fromString(resultSet.getString("lid"));
                        String username = resultSet.getString("lusername");
                        String password = resultSet.getString("lpassword");
                        return new UserCredentials(uid,username,password);
                    });
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }

    private Optional<User> selectUserById(UUID id){
        final String sql = "SELECT * FROM users INNER JOIN login ON uid=lid INNER JOIN roles ON urole=rid WHERE uid = ?";
        try{
            User user = jdbcTemplate.queryForObject(sql, new Object[]{id},
                    (resultSet, i) -> {
                        String name = resultSet.getString("uname");
                        String phone = resultSet.getString("uphone");
                        Date birthDate = resultSet.getDate("ubirthday");
                        String username = resultSet.getString("lusername");
                        String password = resultSet.getString("lpassword");
                        String role = resultSet.getString("rname");
                        return new User(id,name,username,password,phone,birthDate,User.setUserRoles(role),
                                true,true,
                                true,true);
                    });
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }

    @Override
    public Optional<User> loadUserByUsername(String username) {
        final String sql = "SELECT * FROM login WHERE lusername = ?";
        try{
            UserCredentials userCredentials = jdbcTemplate.queryForObject(sql, new Object[]{username},
                    (resultSet, i) -> {
                        UUID uid = UUID.fromString(resultSet.getString("lid"));
                        String currentUsername = resultSet.getString("lusername");
                        String password = resultSet.getString("lpassword");
                        return new UserCredentials(uid,currentUsername,password);
                    });
            assert userCredentials != null;

            User user = selectUserById(userCredentials.getId())
                    .orElseThrow(() -> new ApiRequestException("UserView not found"));


            return Optional.of(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }

    }

    @Override
    public UUID getUserIdByCredentials(String username) {
        final String sql = "SELECT lid FROM login WHERE lusername = ?";
        try {
            return jdbcTemplate.queryForObject(sql, new Object[]{username},
                    (resultSet, i) -> UUID.fromString(resultSet.getString("lid")));
        } catch (EmptyResultDataAccessException e) {
            return null;
        }
    }

    @Override
    public Optional<UserView> verifyUserUUID(UUID userId) {
        final String sql = "SELECT * FROM users WHERE uId = ?";
        try {
            UserView user = jdbcTemplate.queryForObject(sql, new Object[]{userId},
                    (resultSet, i) -> {
                        String name = resultSet.getString("uname");
                        String phone = resultSet.getString("uphone");
                        Date birthDate = resultSet.getDate("ubirthday");
                        return new UserView(name, phone, birthDate);
                    });
            return Optional.ofNullable(user);
        } catch (EmptyResultDataAccessException e) {
            return Optional.empty();
        }
    }
}
