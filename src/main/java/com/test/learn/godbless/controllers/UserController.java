package com.test.learn.godbless.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    @GetMapping(value = "/hello")
    public String loginOrRegisterDisplay(Model model) {
        return new String("loginOrRegister");
    }

    @GetMapping(value = "/error/403")
    public String denied() {
        return new String("/errors/forbidden");
    }

}
