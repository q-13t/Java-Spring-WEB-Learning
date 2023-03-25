package com.test.learn.godbless.models;

import jakarta.annotation.Nonnull;
import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.NotEmpty;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.beans.factory.annotation.Autowired;

public class Fruit {
    private int id;

    @NotNull(message = "Can't be empty")
    private boolean fresh;

    @Size(min = 2, max = 30, message = "Too long, or short. Idk...")
    @NotEmpty(message = "Hey! Gimmi a NAME!")
    @Nonnull
    private String name;

    @Autowired
    private Environment env;

    public String getFreshState() {
        if (fresh)
            return "Fresh";

        return "Moldy";
    }

    public boolean isFresh() {
        return fresh;
    }

    public void setFresh(boolean fresh) {
        this.fresh = fresh;
    }

    public Fruit(int id, String name, boolean fresh) {
        this.id = id;
        this.fresh = fresh;
        this.name = name;
    }

    public Fruit(int id) {
        this.id = id;
    }

    public Fruit(String name) {
        this.name = name;
    }

    public Fruit() {
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return "\nFruit:\t[" + "Id-> " + id + " Name-> " + name + " Fresh-> " + fresh + "]";
    }

    public String getImgName() {
        return new String(getName() + ".png");
        // return new String(getName() + env.getProperty("img.format"));
    }
}
