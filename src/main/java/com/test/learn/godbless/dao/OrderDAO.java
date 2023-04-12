package com.test.learn.godbless.dao;

import java.util.Map;
import java.util.List;
import java.util.HashMap;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.springframework.stereotype.Component;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.beans.factory.annotation.Autowired;

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

    public Map<Integer, List<Order>> getTenOrdersByUsernameAndOffset(String username, Integer offset) {
        Map<Integer, List<Order>> orders = jdbcTemplate
                .query("SELECT * FROM fruits_db.order WHERE username = ? limit ?,10;",
                        new BeanPropertyRowMapper<>(Order.class), username, offset)
                .stream()
                .collect(Collectors.groupingBy(Order::getId));
        while (orders.isEmpty() && offset > 0) {
            offset -= 10;
            orders = jdbcTemplate.query("SELECT * FROM fruits_db.order WHERE username = ? limit ?,10;",
                    new BeanPropertyRowMapper<>(Order.class), username, offset).stream()
                    .collect(Collectors.groupingBy(Order::getId));
        }
        orders.values().forEach(list -> {
            for (Order order : list) {
                order.setFruit(fruitDAO.getById(order.getFruit_id()));
            }
        });
        return orders;
    }

    public void updateOrdersUsername(String username_old, String username) {
        jdbcTemplate.update("Update fruits_db.order set username =? where username = ?;", username, username_old);
    }
}