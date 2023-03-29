package com.test.learn.godbless.controllers;

import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;

import com.test.learn.godbless.dao.UserDAO;
import com.test.learn.godbless.models.User;

@Controller
@RequestMapping("/admin")
public class AdminController {

    @Autowired
    private UserDAO userDAO;

    @GetMapping(value = "")
    public String getAdminPage(Model model) {
        model.addAttribute("display", "none");
        return new String("admin/admin");
    }

    @GetMapping(value = "/users")
    public String getUsers(Model model) {
        // List<User> users = userDAO.getAllUsers();
        // List<SimpleGrantedAuthority> authorities = userDAO.getAllAuthorities();
        // for (User user : users) {
        // for (SimpleGrantedAuthority authority : authorities) {
        // System.out.println(user.getAuthority().contains(authority));
        // }
        // }
        model.addAttribute("display", "users");
        model.addAttribute("users", userDAO.getAllUsers());
        model.addAttribute("authorities", userDAO.getAllAuthorities());
        return new String("admin/admin");
    }

    @GetMapping(value = "/products")
    public String getProducts(Model model) {
        model.addAttribute("display", "products");
        return new String("admin/admin");
    }

    @PostMapping(value = "/updateUser")
    public String postMethodName(@ModelAttribute("user") User user, @ModelAttribute("user-pre") String original_user) {
        System.out.println(original_user + " -> " + user);
        return "redirect:/admin/users";
    }

}
