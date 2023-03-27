package com.test.learn.godbless.config;

import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.Authentication;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;

import com.test.learn.godbless.models.User;

@Component
public class UserAuthenticator implements AuthenticationProvider {

    @Autowired
    private JdbcTemplate jdbctemplate;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {
        try {
            User user = jdbctemplate.queryForObject(
                    "SELECT u.username,u.password,a.authority FROM users u INNER JOIN authorities a ON a.username = u.username WHERE u.username=?;",
                    new BeanPropertyRowMapper<>(User.class), authentication.getName());
            if (user != null) {
                return new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword(),
                        user.getAuthority());
            }
            return null;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}