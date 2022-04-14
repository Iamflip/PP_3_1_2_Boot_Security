package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.DeleteMapping;
import ru.kata.spring.boot_security.demo.models.User;
import ru.kata.spring.boot_security.demo.services.RoleService;
import ru.kata.spring.boot_security.demo.services.UserService;
import java.security.Principal;
import java.util.List;

@RequestMapping()
@RestController
public class AdminController {
    private final UserService userService;
    private final RoleService roleService;

    public AdminController(UserService userService, RoleService roleService) {
        this.userService = userService;
        this.roleService = roleService;
    }

    @GetMapping("/admin")
    public List<User> userList() {
        return userService.allUsers();
    }

    @GetMapping("/admin/{id}")
    public User findUserById(@PathVariable("id") Long id) {
        return userService.findUserById(id);
    }

    @PostMapping("/admin")
    public void addUser(@RequestBody User user) {
        userService.saveUser(user);
    }

    @PutMapping("/admin")
    public void updateUser(@RequestBody User user) {
        userService.saveUser(user);
    }

    @DeleteMapping("/admin/{id}")
    public void deleteUser(@PathVariable("id") Long id) {
        userService.deleteUser(id);
    }

    @GetMapping("/authUser")
    public UserDetails getUserInfo(Principal principal) {
        return userService.loadUserByUsername(principal.getName());
    }

}
