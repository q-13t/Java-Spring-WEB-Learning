package com.test.learn.godbless.controllers;

import org.springframework.ui.Model;
import com.test.learn.godbless.dao.FruitDAO;
import com.test.learn.godbless.dao.UserDAO;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
public class MainController {
    @Autowired
    private FruitDAO fruitDAO;
    @Autowired
    private UserDAO userDAO;

    @GetMapping(value = "/")
    public String index(Model model) {

        if (!userDAO.testConnection()) {
            return "redirect:/error/notConnected";
        }

        model.addAttribute("fruits", fruitDAO.getAllFruits());

        try {
            String user = SecurityContextHolder.getContext().getAuthentication().getName();
            if (user != "anonymousUser") {
                model.addAttribute("username", user);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        // System.out.println(model.getAttribute("username"));
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
