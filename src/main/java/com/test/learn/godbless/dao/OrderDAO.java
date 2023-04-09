package com.test.learn.godbless.dao;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

@Component
public class OrderDAO {
    private final JdbcTemplate jdbcTemplate;

    public OrderDAO(JdbcTemplate template) {
        jdbcTemplate = template;
    }

    private long getHighestOrderID() {
        Long maxID = jdbcTemplate.queryForObject("SELECT MAX(id) FROM fruits_db.order;", Long.class);
        return (maxID == null) ? 1 : maxID + 1;
    }

    public Entry<Long, String> addOrder(HashMap<Integer, Integer> order, String username, String address) {
        Long id = getHighestOrderID();

        order.forEach((x, y) -> {
            jdbcTemplate.update("INSERT INTO fruits_db.order(id,fruit_id,amount,username,address) VALUE(?,?,?,?,?);",
                    id,
                    x,
                    y,
                    username,
                    address);
        });

        return Map.entry(id, address);
    }
}
