package com.test.learn.godbless.models;

import java.util.ArrayList;

import jakarta.validation.constraints.Size;
import jakarta.validation.constraints.NotBlank;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

public class User {
    @NotBlank(message = "UserName Can't be empty!")
    @Size(min = 4, max = 20, message = "UserName length must be beetween 4 and 20 caracters!")
    private String username;

    @NotBlank(message = "Password Is Requiered!")
    @Size(min = 4, max = 20, message = "Passwords length must be beetween 4 and 20 caracters!")
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

    @Override
    public String toString() {
        return "USER->[ " + getUsername() + " , " + getPassword() + " , " + getAuthority() + " ]";
    }

}
