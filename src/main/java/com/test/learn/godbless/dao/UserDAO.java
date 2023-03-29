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
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.test.learn.godbless.config.UserAuthenticator;
import com.test.learn.godbless.models.User;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

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
        HttpSession session = session().getSession(true);
        session.setAttribute("SPRING_SECURITY_CONTEXT", securityContext);
    }

    public void register(User user) {
        if (!testConnection())
            return;

        System.out.println(checkIfUserRegistered(user) ? "Exists" : "Does Not Exist");
        if (!checkIfUserRegistered(user)) {
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

    public boolean checkIfUserRegistered(User user) {
        try {
            jdbctemplate.queryForObject("SELECT * FROM users WHERE USERNAME=?", new BeanPropertyRowMapper<>(User.class),
                    user.getUsername());
        } catch (Exception e) {
            return false;
        }
        return true;
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

    public boolean testConnection() {
        try {
            jdbctemplate.query("SELECT 1 FROM fruit;", new BeanPropertyRowMapper<>(String.class));
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}