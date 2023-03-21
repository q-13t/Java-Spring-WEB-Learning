package com.test.learn.godbless.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class MainController {

    @GetMapping(value = "/")
    public String sendMessage() {
        return "index";
    }

    @GetMapping("/hi")
    public String hello() {
        return "main/hello";
    }

    @GetMapping("/bb")
    public String bbye() {
        return "main/bye";
    }
}
