package com.test.learn.godbless.dao;

import java.util.Collections;
import java.util.List;

import org.springframework.dao.DataAccessException;
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

    /**
     * SHOULD BE OMITTED
     * 
     * @return
     */
    public List<Fruit> getAllFruits() {
        return jdbcTemplate.query("SELECT * FROM fruit;", new BeanPropertyRowMapper<>(Fruit.class));
    }

    public List<Fruit> getTenFruitsByOffset(int offset) {
        List<Fruit> list = jdbcTemplate.query("SELECT * FROM fruit limit ?,10;",
                new BeanPropertyRowMapper<>(Fruit.class), offset);
        while (list.isEmpty() && offset > 0) {
            offset -= 10;
            list = jdbcTemplate.query("SELECT * FROM fruit LIMIT ?, 10;",
                    new BeanPropertyRowMapper<>(Fruit.class), offset);
        }
        return list;
    }

    public List<Fruit> getListById(List<Integer> ids) {
        String placeholders = String.join(",", Collections.nCopies(ids.size(), "?"));
        String query = "SELECT id, name, fresh, image from fruit where ID in (" + placeholders + ")";
        return jdbcTemplate.query(query, new BeanPropertyRowMapper<>(Fruit.class),
                ids.toArray());
    }

    public Fruit getById(int id) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM fruit WHERE id=?;",
                    new BeanPropertyRowMapper<>(Fruit.class),
                    id);
        } catch (Exception e) {
            return null;
        }
    }

    public Fruit getByName(String name) {
        try {
            return jdbcTemplate.queryForObject(
                    "SELECT * FROM fruit WHERE name=?;",
                    new BeanPropertyRowMapper<>(Fruit.class),
                    name);
        } catch (Exception e) {
            return null;
        }
    }

    public void add(Fruit fruit) {
        jdbcTemplate.update("INSERT INTO fruit(name,fresh,image) VALUE(?,?,?)", fruit.getName(), fruit.isFresh(),
                fruit.getImgName());
    }

    public void update(Fruit fruit) {
        jdbcTemplate.update("UPDATE fruit SET name =?,fresh=? WHERE id=?;", fruit.getName(), fruit.isFresh(),
                fruit.getId());
    }

    public void updateById(int id, Fruit fruit) {
        jdbcTemplate.update("update fruit set name=?, fresh = ?, image =? where id = ?;", fruit.getName(),
                fruit.isFresh(), fruit.getImgName().equals("") ? null : fruit.getImgName(), id);
    }

    public void delete(int id) {
        jdbcTemplate.update("DELETE FROM fruit WHERE id = ?;", id);
    }

    public List<Boolean> getAllFreshStates() {
        List<Boolean> freshFruits = null;

        try {
            freshFruits = jdbcTemplate.query(
                    "SELECT DISTINCT fresh FROM fruit",
                    (rs, rowNum) -> rs.getBoolean("fresh"));
        } catch (DataAccessException e) {
            e.printStackTrace();
        }

        return freshFruits;
    }

}
