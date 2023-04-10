package com.test.learn.godbless.models;

public class Order {
    private int id;
    private int fruit_id;
    private int amount;
    private String username;
    private String address;
    private Fruit fruit;

    public Fruit getFruit() {
        return fruit;
    }

    public void setFruit(Fruit fruit) {
        this.fruit = fruit;
    }

    public Order(int id, int fruit_id, int amount, String username, String address) {
        this.id = id;
        this.fruit_id = fruit_id;
        this.amount = amount;
        this.username = username;
        this.address = address;
    }

    public Order() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getFruit_id() {
        return fruit_id;
    }

    public void setFruit_id(int fruit_id) {
        this.fruit_id = fruit_id;
    }

    public int getAmount() {
        return amount;
    }

    public void setAmount(int amount) {
        this.amount = amount;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    @Override
    public String toString() {
        return "Order: [id: " + id + ", fruit_id: " + fruit_id + ", amount: " + amount + ", username: " + username
                + ", address: " + address + "]";
    }

}
