package com.test.learn.godbless.controllers;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping(value = "/hello")
    public String loginOrRegisterDisplay(Model model) {
        return new String("loginOrRegister");
    }

    @GetMapping(value = "/error/forbidden")
    public String denied() {
        return new String("/errors/forbidden");
    }

    @GetMapping(value = "/error/notConnected")
    public String notConnected() {
        return new String("/errors/connection");
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public String register(@RequestParam(value = "username_register") String userame,
            @RequestParam(value = "password_register") String password,
            Model model) {
        System.out.println("register");
        System.out.println("User-> " + userame + " | " + password);
        if (password == "") {
            model.addAttribute("reg_error", true);
            model.addAttribute("username_register", userame);
            return new String("loginOrRegister");
        }
        return new String("redirect:/");
    }
}
