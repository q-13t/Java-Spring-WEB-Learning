package com.test.learn.godbless.dao;

import java.util.List;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import com.test.learn.godbless.models.Fruit;

@Component
public class FruitDAO {

    private final JdbcTemplate jdbcTemplate;

    public FruitDAO(JdbcTemplate template) {
        jdbcTemplate = template;
    }

    public List<Fruit> getAll() {
        return jdbcTemplate.query("SELECT * FROM fruit;", new BeanPropertyRowMapper<>(Fruit.class));
    }

    public Fruit getById(int id) {
        return jdbcTemplate.queryForObject(
                "SELECT * FROM fruit WHERE id=?;",
                new BeanPropertyRowMapper<>(Fruit.class),
                id);
    }

    public void add(Fruit fruit) {
        jdbcTemplate.update("INSERT INTO fruit(name,fresh) VALUE(?,?)", fruit.getName(), fruit.isFresh());
    }

    public void update(Fruit fruit) {
        jdbcTemplate.update("UPDATE fruit SET name =?,fresh=? WHERE id=?;", fruit.getName(), fruit.isFresh(),
                fruit.getId());
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM fruit WHERE id = ?;", id);
    }
}
