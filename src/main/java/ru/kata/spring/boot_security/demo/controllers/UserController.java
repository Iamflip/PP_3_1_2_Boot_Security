package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.UserService;

@Controller
public class UserController {
    @Autowired
    private UserService userService;
    @GetMapping("/user")
    public String getUser(Model model) {
        User userInfo = userService.getUserInfo();
        model.addAttribute("userInfo", userInfo);
        return "user";
    }

    @GetMapping("/user/{id}/edit")
    public String getEditForm(@PathVariable("id") Long id, Model model){
        model.addAttribute("user", userService.findUserById(id));
        return "update.html";
    }

    @PatchMapping("/user")
    public String updateUser(User user) {
        userService.saveUser(user);
        return "redirect:/user";
    }
}
