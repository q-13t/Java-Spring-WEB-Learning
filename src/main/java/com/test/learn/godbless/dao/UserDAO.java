package com.test.learn.godbless.dao;

import java.sql.Connection;
import java.util.List;

import org.springframework.context.annotation.Bean;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.stereotype.Component;

import com.test.learn.godbless.models.Fruit;

@Component
public class UserDAO {

    private final JdbcTemplate jdbctemplate;

    public UserDAO(JdbcTemplate template) {
        jdbctemplate = template;
    }

    @Bean
    public boolean testConnection() {
        try {
            jdbctemplate.query("SELECT 1 FROM fruit;", new BeanPropertyRowMapper<>(String.class));
        } catch (Exception e) {
            return false;
        }
        return true;
    }
}