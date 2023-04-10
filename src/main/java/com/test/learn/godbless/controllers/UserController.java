package com.test.learn.godbless.controllers;

import java.util.Map;
import java.util.List;
import java.io.IOException;
import java.util.stream.Collectors;

import org.springframework.ui.Model;
import org.springframework.http.HttpStatus;
import org.springframework.web.servlet.View;
import org.springframework.stereotype.Controller;
import org.springframework.validation.BindingResult;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.beans.factory.annotation.Autowired;

import com.test.learn.godbless.models.User;
import com.test.learn.godbless.dao.UserDAO;
import com.test.learn.godbless.dao.OrderDAO;
import com.test.learn.godbless.models.Order;
import com.test.learn.godbless.exceptions.PasswordException;
import com.test.learn.godbless.exceptions.UsernameException;

import jakarta.validation.Valid;
import jakarta.servlet.http.HttpSession;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Controller
public class UserController {
    @Autowired
    private UserDAO userDAO;

    @Autowired
    private OrderDAO orderDAO;

    @Autowired
    HttpSession session;

    @GetMapping(value = "/hello")
    public String loginOrRegisterDisplay(HttpServletRequest request, HttpServletResponse response, Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("login_user", new User());
        return new String("loginOrRegister");
    }

    @PostMapping("/validateUser")
    public ModelAndView validateUser(@ModelAttribute(value = "login_user") User user,
            HttpServletRequest request, HttpServletResponse response, HttpSession session,
            Model model) throws IOException {
        try {
            userDAO.validateUser(user);
        } catch (UsernameException | PasswordException e) {
            System.out.println(e.getMessage());
            ModelAndView mav = new ModelAndView("loginOrRegister");
            mav.addObject("error", e.getMessage());
            mav.addObject("user", new User());
            mav.addObject("login_user", user);
            return mav;
        }

        String redirectURL = "redirect:/" + (((StringBuffer) session.getAttribute("redirectURL")).toString())
                .replaceAll("(http://localhost:8080/|\\.html)", "");
        System.out.println("login: " + redirectURL);
        if (redirectURL.equals("redirect:/login")) {
            redirectURL = "redirect:/";
            ModelAndView mav = new ModelAndView(redirectURL);
            session.removeAttribute("redirectURL");
            session.removeAttribute("redirectParameters");
            return mav;
        }
        ModelAndView mav = new ModelAndView(redirectURL);
        request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);

        Map<?, ?> redirectParameters = (Map<?, ?>) session
                .getAttribute("redirectParameters");

        redirectParameters.forEach((x, y) -> {
            System.out.println(x + " -> " + y);
            mav.addObject((String) x, (String[]) y);
        });
        session.removeAttribute("redirectURL");
        session.removeAttribute("redirectParameters");
        return mav;
    }

    @RequestMapping(value = "/register", method = RequestMethod.POST)
    public ModelAndView register(@ModelAttribute(value = "user") @Valid User user, BindingResult bindingResult,
            HttpServletRequest request,
            Model model) {
        if (!bindingResult.hasErrors()) {
            userDAO.register(user);
        } else {
            ModelAndView mav = new ModelAndView("loginOrRegister");
            mav.addObject("user", user);
            mav.addObject("login_user", new User());
            return mav;
        }

        String redirectURL = "redirect:/" + (((StringBuffer) session.getAttribute("redirectURL")).toString())
                .replaceAll("(http://localhost:8080/|\\.html)", "");
        System.out.println("register: " + redirectURL);

        if (redirectURL.equals("redirect:/login")) {
            redirectURL = "redirect:/";
            ModelAndView mav = new ModelAndView(redirectURL);
            session.removeAttribute("redirectURL");
            session.removeAttribute("redirectParameters");
            return mav;
        }
        ModelAndView mav = new ModelAndView(redirectURL);
        request.setAttribute(View.RESPONSE_STATUS_ATTRIBUTE, HttpStatus.TEMPORARY_REDIRECT);

        Map<?, ?> redirectParameters = (Map<?, ?>) session
                .getAttribute("redirectParameters");

        redirectParameters.forEach((x, y) -> {
            System.out.println(x + " -> " + y);
            mav.addObject((String) x, (String[]) y);
        });
        session.removeAttribute("redirectURL");
        session.removeAttribute("redirectParameters");
        return mav;
    }

    @RequestMapping(value = "/user/{username}")
    public String getUserPage(@PathVariable("username") String username, Model model) {
        Map<Integer, List<Order>> orders = orderDAO.getOrdersByUsername(username)
                .stream()
                .collect(Collectors.groupingBy(Order::getId));
        model.addAttribute("username", username);
        model.addAttribute("orders", orders);
        return "userPage";
    }

    @PostMapping(value = "/user/update")
    public String updateUsername(@ModelAttribute("username_new") String username_new,
            @ModelAttribute("username_old") String username_old, @ModelAttribute("action") String action) {
        System.out.println("Username old: " + username_old);
        System.out.println("Username new: " + username_new);
        System.out.println("action: " + action);
        switch (action) {
            case "Save!": {
                userDAO.updateUserByName(username_old, username_new);
                break;
            }
            case "Delete!": {
                userDAO.deleteUserByName(username_old);
                userDAO.unAuthenticate();
                return "redirect:/";
            }
            default: {
                return "redirect:/user/" + username_old;
            }
        }
        return "redirect:/user/" + username_new;
    }

    @GetMapping(value = "/error/forbidden")
    public String accessDenied() {
        return new String("/errors/forbidden");
    }

    @GetMapping(value = "/error/notConnected")
    public String notConnected() {
        if (userDAO.testConnection()) {
            return "redirect:/";
        }
        return new String("/errors/connection");
    }
}
