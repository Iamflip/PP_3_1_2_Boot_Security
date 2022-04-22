package ru.kata.spring.boot_security.demo.DTO;

import lombok.Data;
import ru.kata.spring.boot_security.demo.models.Role;

import java.util.Set;

@Data
public class UserDto {
    private Long id;
    private String firstName;
    private String lastName;
    private Integer age;
    private String username;
    private String password;
    Set<Role> roles;
}
