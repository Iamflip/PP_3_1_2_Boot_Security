package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.HashSet;
import java.util.Set;

@Controller
public class RegistrationController {

    private final UserService userService;
    private final RoleService roleService;

    public RegistrationController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/registration")
    public String registration(User user) {

        return "registration.html";
    }

    @PostMapping("/registration")
    public String addUser(@ModelAttribute("user")User user, @RequestParam("newRoles[]") String[] roles) {
        Set<Role> roleSet = new HashSet<>();
        for (String role : roles){
            roleSet.add(roleService.getRoleByName(role));
        }

        user.setRoles(roleSet);
        userService.saveUser(user);

        return "redirect:/admin";
    }
}
