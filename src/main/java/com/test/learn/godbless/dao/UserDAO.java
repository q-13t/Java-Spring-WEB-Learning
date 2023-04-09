package com.test.learn.godbless.dao;

import java.util.List;

import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.test.learn.godbless.models.User;
import com.test.learn.godbless.config.UserAuthenticator;
import com.test.learn.godbless.exceptions.PasswordException;
import com.test.learn.godbless.exceptions.UsernameException;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class UserDAO {

    @Autowired
    private JdbcTemplate jdbctemplate;

    @Autowired
    private UserAuthenticator authenticator;

    public static HttpServletRequest session() {
        ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
        return attr.getRequest();
    }

    public void authenticate(User user) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        securityContext.setAuthentication(authenticator
                .authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword())));
        session().getSession(true).setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
    }

    public void validateUser(User user) throws UsernameException, PasswordException {
        if (!isUserRegistered(user)) {
            throw new UsernameException("Provided username was not found!");
        }
        if (!isUserPasswordCorrect(user)) {
            throw new PasswordException("Provided password is incorrect!");
        }
        authenticate(user);
    }

    public void register(User user) {
        if (!testConnection())
            return;

        System.out.println(isUserRegistered(user) ? "Exists" : "Does Not Exist");
        if (!isUserRegistered(user)) {
            addToDataBase(user);
        }
        authenticate(user);
    }

    private void addToDataBase(User user) {
        jdbctemplate.update("INSERT INTO users(username,password,enabled) values(?,?,?);", user.getUsername(),
                user.getPassword(), "Y");
        jdbctemplate.update("INSERT INTO authorities(username,authority) values(?,?);", user.getUsername(),
                "ROLE_USER");
    }

    public boolean isUserRegistered(User user) {
        try {
            jdbctemplate.queryForObject("SELECT * FROM users WHERE USERNAME=?", new BeanPropertyRowMapper<>(User.class),
                    user.getUsername());
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public boolean isUserPasswordCorrect(User user) {
        try {
            User user_db = getByUsername(user.getUsername());
            if (user.getPassword().equals(user_db.getPassword())) {
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    public List<SimpleGrantedAuthority> getAllAuthorities() {
        return jdbctemplate.queryForList("SELECT DISTINCT authority from authorities;", SimpleGrantedAuthority.class);
    }

    public List<User> getAllUsers() {
        testConnection();
        return jdbctemplate.query(
                "SELECT u.username, u.password, a.authority FROM users u INNER JOIN authorities a ON a.username = u.username;",
                new BeanPropertyRowMapper<>(User.class));
    }

    public User getByUsername(String username) {
        try {
            return jdbctemplate.queryForObject(
                    "SELECT u.username, u.password, a.authority FROM users u INNER JOIN authorities a ON a.username = u.username WHERE u.USERNAME = ?;",
                    new BeanPropertyRowMapper<>(User.class), username);
        } catch (Exception e) {
            return null;
        }
    }

    public boolean testConnection() {
        try {
            jdbctemplate.query("SELECT 1 FROM fruit;", new BeanPropertyRowMapper<>(String.class));
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public void deleteUserByName(String original_user) {
        jdbctemplate.update("DELETE FROM authorities where username = ?;", original_user);
        jdbctemplate.update("DELETE FROM users where username = ?;", original_user);
    }

    public void updateUserByName(String original_user, User user) {
        jdbctemplate.update("SET GLOBAL FOREIGN_KEY_CHECKS=0;");

        jdbctemplate.update(
                "update users u Inner JOIN authorities a ON a.username = u.username set  u.username = ?, a.username =?, u.password = ?, a.authority = ? where u.username =  ?;",
                user.getUsername(), user.getUsername(), user.getPassword(), user.getAuthority().get(0).getAuthority(),
                original_user);

        jdbctemplate.update("SET GLOBAL FOREIGN_KEY_CHECKS=1;");

    }

    public Authentication getCurrentAuthentication() {
        return SecurityContextHolder.getContext().getAuthentication();
    }

    public String getCurrentUsername() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}