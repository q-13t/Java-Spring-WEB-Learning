package com.test.learn.godbless.controllers;

import java.util.HashMap;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.test.learn.godbless.dao.FruitDAO;
import com.test.learn.godbless.dao.UserDAO;

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
        return "index";
    }

    @PostMapping("/purchase")
    public String processPurchaseData(@RequestParam("order-data") String mapString, Model model) {
        HashMap<Integer, Integer> order = Stream.of(mapString.split(","))
                .map(x -> x.split(":"))
                .collect(Collectors.toMap(
                        x -> Integer.parseInt(x[0]),
                        x -> Integer.parseInt(x[1]),
                        (a, b) -> a,
                        HashMap::new));

        order.forEach((fruitId, amount) -> System.out.println(fruitId + " " + amount));

        return "confirmOrder";
    }
}
