package com.yogesh.blog.controllers;

import com.yogesh.blog.model.Role;
import com.yogesh.blog.model.User;
import com.yogesh.blog.services.RoleService;
import com.yogesh.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("user")
public class UserController {
    private final UserService userService;
    private final RoleService roleService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public UserController(UserService userService, RoleService roleService,PasswordEncoder passwordEncoder) {
        this.userService = userService;
        this.roleService = roleService;
        this.passwordEncoder = passwordEncoder;
    }

    @GetMapping("sign-in")
    String showFormForSignIn(Model model) {
        model.addAttribute("user", new User());
        return "sign-in";
    }

    @GetMapping("sign-up")
    String showFormForSignUp(Model model) {
        model.addAttribute("user", new User());
        return "sign-up";
    }

    @PostMapping("save-user")
    String saveUser(@ModelAttribute("user") User user, @RequestParam(name = "authority", required = false) List<String> roles) {
        List<Role> userRoles = new ArrayList<>();
        roles.forEach(roleName -> {
            Role role = roleService.findRoleByName(roleName).orElse(new Role(roleName));
            userRoles.add(role);
        });
        String encryptedPassword = passwordEncoder.encode(user.getPassword());
        user.setPassword(encryptedPassword);
        user.setRoles(userRoles);
        userService.saveUser(user);
        return "redirect:/user/sign-in";
    }
}
