package com.test.learn.godbless.controllers;

import org.springframework.ui.Model;
import com.test.learn.godbless.dao.UserDAO;
import com.test.learn.godbless.models.User;
import org.springframework.stereotype.Controller;
import org.springframework.context.ApplicationContext;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    ApplicationContext context;

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
    public String postMethodName(
            @ModelAttribute("user") User user,
            @ModelAttribute("user-pre") String original_user,
            @ModelAttribute("action") String action,
            Model model) {

        if (original_user.matches("(admin)|(user)")) {
            model.addAttribute("display", "users");
            model.addAttribute("hint", "Cannot perform actions on [user,admin]!");
            model.addAttribute("users", userDAO.getAllUsers());
            model.addAttribute("authorities", userDAO.getAllAuthorities());
            return "admin/admin";
        }

        switch (action) {
            case "Delete!": {
                System.out.println("Deleting" + user);
                userDAO.deleteUserByName(original_user);
                break;
            }
            case "Save!": {
                System.out.println("Updating From " + original_user + " To " + user);
                userDAO.updateUserByName(original_user, user);
                break;
            }
            default: {
                System.out.println("No action Defined");
                break;
            }
        }
        return "redirect:/admin/users";
    }

    @RequestMapping(value = "/findUser", method = RequestMethod.POST)
    public String findUser(@ModelAttribute(value = "username_search") String username, Model model) {
        if (username.equals("")) {
            return "redirect:/admin/users";
        }

        User user;
        if ((user = userDAO.getByUsername(username)) != null) {
            model.addAttribute("users", user);
            model.addAttribute("authorities", userDAO.getAllAuthorities());
        } else {
            model.addAttribute("users", null);
        }
        model.addAttribute("display", "users");
        return "admin/admin";
    }
}
