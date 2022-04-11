package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;

import java.util.HashSet;
import java.util.Set;

@Controller
public class AdminController {
    private final UserService userService;

    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/admin")
    public String userList(Model model) {
        User userInfo = userService.getUserInfo();
        model.addAttribute("userInfo", userInfo);
        model.addAttribute("allUsers", userService.allUsers());
        return "admin";
    }

    @GetMapping("/admin/{id}/edit")
    public String getEditForm(@PathVariable("id") Long id, Model model){
        model.addAttribute("user", userService.findUserById(id));
        return "updateAdmin.html";
    }

    @PatchMapping("/admin")
    public String updateUser(@ModelAttribute("user") User user, @RequestParam("newRoles[]") String[] roles) {
        Set<Role> roleSet = new HashSet<>();
        for (String role : roles){
            roleSet.add(roleService.getRoleByName(role));
        }
        user.setRoles(roleSet);

        userService.saveUser(user);
        return "redirect:/admin";
    }

    @DeleteMapping("/admin")
    public String deleteUser(@RequestParam("id") Long id) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }

}
