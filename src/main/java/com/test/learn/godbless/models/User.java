package com.test.learn.godbless.models;

import java.util.ArrayList;

import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class User {
    private String username;
    private String password;
    private ArrayList<SimpleGrantedAuthority> authority;

    public ArrayList<SimpleGrantedAuthority> getAuthority() {
        return authority;
    }

    public void setAuthority(ArrayList<SimpleGrantedAuthority> authority) {
        this.authority = authority;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
