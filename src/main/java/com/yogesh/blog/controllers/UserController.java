package com.yogesh.blog.controllers;


import com.yogesh.blog.entities.User;
import com.yogesh.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping
public class UserController {
    private final UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping("sign-in")
    String signInUser(Model model){
        model.addAttribute("user",new User());
        return "sign-in";
    }

    @GetMapping("sign-up")
    String signUpUser(Model model){
        model.addAttribute("user",new User());
        return "sign-up";
    }

    @PostMapping("save-user")
    String saveUser(@ModelAttribute("user") User user){
        userService.addUser(user);
        return "redirect:/sign-in";
    }

    @PostMapping("check-user")
    String checkAuth(@ModelAttribute("user") User user){
        return "redirect:/feed";
    }
}
