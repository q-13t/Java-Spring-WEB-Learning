package com.test.learn.godbless.controllers;

import jakarta.validation.Valid;
import org.springframework.ui.Model;
import com.test.learn.godbless.dao.FruitDAO;
import com.test.learn.godbless.models.Fruit;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/fruits")
public class FruitsController {

    @Autowired
    private FruitDAO fruitDAO;

    @GetMapping(value = "")
    public String index(@RequestParam(value = "id", defaultValue = "-1") int id, Model model) {
        if (id < 0) {
            model.addAttribute("fruits", fruitDAO.getAllFruits());
        } else {
            model.addAttribute("fruits", fruitDAO.getById(id));
        }
        return new String("/fruits/main");
    }

    @GetMapping(value = "/{id}")
    public String showFruitData(@PathVariable("id") int id, Model model) {
        model.addAttribute("fruit", fruitDAO.getById(id));
        return new String("/fruits/fruit");
    }

    @GetMapping(value = "/add")
    public String displayNewFruitWindow(Model model) {
        model.addAttribute("fruit", new Fruit());
        return new String("/fruits/new");
    }

    @PostMapping(value = "")
    public String addFruit(@ModelAttribute("fruit") @Valid Fruit fruit, BindingResult bindingResult) {
        if (!bindingResult.hasErrors()) {
            fruitDAO.add(fruit);
        } else {
            return new String("/fruits/new");
        }
        return new String("redirect:/fruits");
    }

    @PostMapping(value = "/{id}")
    public String updateFruit(@ModelAttribute("fruit") @Valid Fruit fruit, BindingResult bindingResult,
            HttpServletRequest request) {

        if (request.getParameter("update") != null) {
            if (!bindingResult.hasErrors())
                fruitDAO.update(fruit);
        } else {
            fruitDAO.delete(fruit.getId());
        }
        return "redirect:/fruits";
    }

}