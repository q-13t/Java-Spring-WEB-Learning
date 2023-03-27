package com.test.learn.godbless.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import com.test.learn.godbless.dao.UserDAO;
import com.test.learn.godbless.models.User;

@Controller
public class UserController {
    @Autowired
    private UserDAO userDAO;

    @GetMapping(value = "/hello")
    public String loginOrRegisterDisplay(Model model) {
        System.out.println("login");
        model.addAttribute("user", new User());
        return new String("loginOrRegister");
    }

    @GetMapping(value = "/error/forbidden")
    public String denied() {
        return new String("/errors/forbidden");
    }

    @GetMapping(value = "/error/notConnected")
    public String notConnected() {
        if (userDAO.testConnection()) {
            return "redirect:/";
        }
        return new String("/errors/connection");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@ModelAttribute(value = "user") User user, Model model) {
        System.out.println("register");
        System.out.println("USER-> " + user.getUsername() + " | " + user.getPassword());
        userDAO.register(user);

        return new String("redirect:/");
    }
}
