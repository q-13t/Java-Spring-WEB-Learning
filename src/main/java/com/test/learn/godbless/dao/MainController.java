package com.test.learn.godbless.dao;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Stream;
import java.util.stream.Collectors;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.test.learn.godbless.models.Fruit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;

@Controller
public class MainController {
    @Autowired
    private FruitDAO fruitDAO;
    @Autowired
    private UserDAO userDAO;
    @Autowired
    private OrderDAO orderDAO;

    @GetMapping(value = "/")
    public String getIndex(Model model) {
        if (!userDAO.testConnection()) {
            return "redirect:/error/notConnected";
        }

        model.addAttribute("fruits", fruitDAO.getAllFruits());

        String user = SecurityContextHolder.getContext().getAuthentication().getName();
        if (user != "anonymousUser") {
            model.addAttribute("username", user);
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
                Stream.of(order.replaceAll("\\{|\\}", "").split(", ")).map(x -> x.split("=")).collect(
                        Collectors.toMap(x -> Integer.parseInt(x[0]), x -> Integer.parseInt(x[1]),
                                (x, y) -> x,
                                HashMap::new)));
        model.addAttribute("fruits", fruitDAO.getAllFruits());

        try {
            String user = userDAO.getCurrentUsername();
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
            System.out.println("Original [" + orderMap + "]");
            HashMap<Integer, Integer> order = parseOrderMap(orderMap);

            order.forEach((fruitId, amount) -> System.out.println(fruitId + " " + amount));
            try {
                String user = userDAO.getCurrentUsername();
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
            @RequestParam("order") String orderMap,
            Model model) {
        HashMap<Integer, Integer> order = parseOrderMap(orderMap);

        Entry<Long, String> orderAdded = orderDAO.addOrder(order, userDAO.getCurrentUsername(),
                country + "/" + address);
        List<Fruit> fruits = fruitDAO.getListById(new ArrayList<>(order.keySet()));
        model.addAttribute("order_id", orderAdded.getKey())
                .addAttribute("order_address", orderAdded.getValue())
                .addAttribute("username", userDAO.getCurrentUsername())
                .addAttribute("order", order)
                .addAttribute("fruits", fruits);
        return "orderStatus";
    }

    private static HashMap<Integer, Integer> parseOrderMap(String map) {
        map = map.replaceAll("\\{|\\}| ", "");
        map = map.replaceAll("=", ":");
        return Stream.of(map.split(","))
                .map(x -> x.split(":"))
                .collect(Collectors.toMap(
                        x -> Integer.parseInt(x[0]),
                        x -> Integer.parseInt(x[1]),
                        (a, b) -> a,
                        HashMap::new));
    }
}
