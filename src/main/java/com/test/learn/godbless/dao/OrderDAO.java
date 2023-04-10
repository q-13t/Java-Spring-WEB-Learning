package com.test.learn.godbless.dao;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.test.learn.godbless.models.Order;

@Component
public class OrderDAO {
    @Autowired
    private FruitDAO fruitDAO;

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

    public List<Order> getOrdersByUsername(String username) {
        List<Order> orders = jdbcTemplate.query("SELECT * FROM fruits_db.order WHERE username = ?;",
                new BeanPropertyRowMapper<>(Order.class), username);
        for (Order order : orders) {
            order.setFruit(fruitDAO.getById(order.getFruit_id()));
        }
        return orders;
    }
}
