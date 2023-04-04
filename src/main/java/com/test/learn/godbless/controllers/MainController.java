package com.test.learn.godbless.controllers;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

import com.fasterxml.jackson.annotation.JsonCreator.Mode;
import com.test.learn.godbless.dao.FruitDAO;
import com.test.learn.godbless.dao.UserDAO;
import com.test.learn.godbless.models.Fruit;

import org.springframework.web.bind.annotation.RequestBody;

@Controller
public class MainController {
    @Autowired
    private FruitDAO fruitDAO;
    @Autowired
    private UserDAO userDAO;

    @GetMapping(value = "/")
    public String getIndex(Model model) {

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

    @PostMapping(value = "/")
    public String postIndex(@RequestParam("order") String order, Model model) {
        if (!userDAO.testConnection()) {
            return "redirect:/error/notConnected";
        }

        System.out.println(order);

        model.addAttribute("order",
                Stream.of(order.replaceAll("\\{|\\}", "").split(",")).map(x -> x.split("=")).collect(
                        Collectors.toMap(x -> Integer.parseInt(x[0]), x -> Integer.parseInt(x[1]), (x, y) -> x,
                                HashMap::new)));
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
    public String processPurchaseData(@RequestParam("order-data") String orderMap, Model model) {
        if (!orderMap.equals("")) {
            HashMap<Integer, Integer> order = Stream.of(orderMap.split(","))
                    .map(x -> x.split(":"))
                    .collect(Collectors.toMap(
                            x -> Integer.parseInt(x[0]),
                            x -> Integer.parseInt(x[1]),
                            (a, b) -> a,
                            HashMap::new));

            order.forEach((fruitId, amount) -> System.out.println(fruitId + " " + amount));
            try {
                String user = SecurityContextHolder.getContext().getAuthentication().getName();
                if (user != "anonymousUser") {
                    model.addAttribute("username", user);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            model.addAttribute("order", order);
            model.addAttribute("fruits", fruitDAO.getListById(Arrays.asList(order.keySet().toArray(new Integer[0]))));
            return "confirmOrder";
        }
        return "redirect:/";
    }

    @PostMapping(value = "/confirmPurchase")
    public String confirmPurchase(@RequestParam("country") String country,
            @RequestParam("address") String address,
            Model model) {

        System.out.println(country + " " + address);

        // TODO implement succesfull purchase

        return "redirect:/";
    }

}
