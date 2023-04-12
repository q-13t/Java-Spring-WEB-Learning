package com.test.learn.godbless.controllers;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.test.learn.godbless.dao.FruitDAO;
import com.test.learn.godbless.dao.UserDAO;
import com.test.learn.godbless.models.Fruit;
import com.test.learn.godbless.models.User;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    ApplicationContext context;

    @Autowired
    private UserDAO userDAO;

    @Autowired
    private FruitDAO fruitDAO;

    @GetMapping(value = "")
    public String getAdminPage(Model model) {
        model.addAttribute("display", "none");
        return new String("admin/admin");
    }

    @GetMapping(value = "/users")
    public String getUsers(@ModelAttribute(value = "action") String action,
            @ModelAttribute(value = "offset") String off, Model model) {
        Integer offset = Integer.valueOf(off.isEmpty() ? "0" : off);
        System.out.println(offset);
        switch (action) {
            case "Next": {
                offset += 10;
                break;
            }
            case "Previous": {
                if (offset - 10 < 0) {
                    offset = 0;
                } else {
                    offset -= 10;
                }
                break;
            }
        }
        model.addAttribute("display", "users");
        model.addAttribute("users", userDAO.getTenUsersByOffset(offset));
        model.addAttribute("authorities", userDAO.getAllAuthorities());
        return new String("admin/admin");
    }

    @PostMapping(value = "/updateUser")
    public String postMethodName(
            @ModelAttribute("user") User user,
            @ModelAttribute("user-pre") String original_user,
            @ModelAttribute("action") String action,
            Model model) {

        if (original_user.matches("(admin|user)")) {
            model.addAttribute("display", "users");
            model.addAttribute("hint", "Cannot perform actions on [user,admin]!");
            model.addAttribute("users", userDAO.getAllUsers());
            model.addAttribute("authorities", userDAO.getAllAuthorities());
            return "admin/admin";
        }

        switch (action) {
            case "Delete!": {
                System.out.println("Deleting " + user);
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
    public String findUser(@RequestParam(value = "username_search") Optional<String> username, Model model) {
        if (username.isPresent() && !username.get().isEmpty()) {
            User user = userDAO.getByUsername(username.get());
            if (user != null) {
                model.addAttribute("users", user);
                model.addAttribute("authorities", userDAO.getAllAuthorities());
            } else {
                model.addAttribute("users", null);
            }
            model.addAttribute("display", "users");
        }

        return "admin/admin";
    }

    @GetMapping(value = "/products")
    public String getProducts(@ModelAttribute(value = "action") String action,
            @ModelAttribute(value = "offset") String off, Model model) {
        Integer offset = Integer.valueOf(off.isEmpty() ? "0" : off);
        System.out.println(offset);
        switch (action) {
            case "Next": {
                offset += 10;
                break;
            }
            case "Previous": {
                if (offset - 10 < 0) {
                    offset = 0;
                } else {
                    offset -= 10;
                }
                break;
            }
        }
        model.addAttribute("display", "products");
        model.addAttribute("fruits", fruitDAO.getTenFruitsByOffset(offset));
        model.addAttribute("freshness", fruitDAO.getAllFreshStates());
        return new String("admin/admin");
    }

    @RequestMapping(value = "/findProduct", method = RequestMethod.POST)
    public String findProduct(@RequestParam("product-search") String search, Model model) {

        Fruit fruit = null;
        if (search != null && !search.isEmpty()) {
            try {
                fruit = fruitDAO.getById(Integer.valueOf(search));
            } catch (NumberFormatException e) {
                fruit = fruitDAO.getByName(search);
            }
        }

        if (fruit != null) {
            model.addAttribute("fruits", fruit);
        } else {
            model.addAttribute("hint", "Nothing was found!");
            model.addAttribute("fruits", fruitDAO.getAllFruits());
        }

        model.addAttribute("display", "products");
        model.addAttribute("freshness", fruitDAO.getAllFreshStates());

        return "admin/admin";
    }

    @GetMapping(value = "/displayNewProduct")
    public String getMethodName(Model model) {
        model.addAttribute("display", "new-product");
        model.addAttribute("fruit", new Fruit());
        model.addAttribute("freshness", fruitDAO.getAllFreshStates());
        return "admin/admin";
    }

    @PostMapping("/addNewProduct")
    public String addProduct(@ModelAttribute("fruit") @Valid Fruit fruit, BindingResult result,
            @RequestParam("image-input") MultipartFile image,
            Model model) throws IOException {
        if (result.hasErrors()) {
            model.addAttribute("display", "new-product");
            model.addAttribute("fruit", fruit);
            model.addAttribute("freshness", fruitDAO.getAllFreshStates());
            return "admin/admin";
        }
        String imagePath = new ClassPathResource("static/imgs/").getFile().getAbsolutePath() + "/";
        String img_name = image.getOriginalFilename();
        if (img_name != null && !img_name.equals("")) {
            fruit.setImage(img_name.replaceAll("(.png)", ""));
            image.transferTo(new File(imagePath + image.getOriginalFilename()));
        } else {
            fruit.setImage(null);
        }
        fruitDAO.add(fruit);
        return "redirect:/admin/products";
    }

    @RequestMapping(value = "/updateProduct", method = RequestMethod.POST)
    public String updateProduct(@ModelAttribute Fruit fruit, @RequestParam("product-original") int id,
            @RequestParam("action") String action,
            @RequestParam("image-input") MultipartFile image, Model model) throws IOException {
        String imagePath = new ClassPathResource("static/imgs/").getFile().getAbsolutePath() + "/";

        if (action.equals("Save!")) {
            if (image != null) {
                String originalFilename = image.getOriginalFilename();
                if (originalFilename != null) {
                    deleteImageFile(fruit.getImgName(), imagePath);
                    try {
                        image.transferTo(new File(imagePath + originalFilename));
                    } catch (IllegalStateException | IOException e) {
                        e.printStackTrace();
                    }
                    fruit.setImage(originalFilename.replaceAll("(.png)", ""));
                }
            }
            fruitDAO.updateById(id, fruit);
        } else if (action.equals("Delete!")) {
            fruitDAO.delete(fruit.getId());
            deleteImageFile(fruit.getImgName(), imagePath);
        }
        return "redirect:/admin/products";
    }

    private void deleteImageFile(String imageName, String imagePath) {
        new File(imagePath + imageName + ".png").delete();
    }
}
