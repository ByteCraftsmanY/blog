package com.yogesh.blog.controllers;


import com.yogesh.blog.entities.User;
import com.yogesh.blog.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("users")
public class UserController {
    private UserService userService;

    @Autowired
    public UserController(UserService userService){
        this.userService = userService;
    }

    @GetMapping
    String getAllUsers(){
        System.out.println("root method is called");
        User user = userService.getUser(1);
        System.out.println(user);
        return "users";
    }

    @PostMapping("adduser")
    String addUser(){
        return "blog-post";
    }

    @GetMapping("getuser")
    String getUser(Model model){
        User user = userService.getUser(1);
        System.out.println(user);
        model.addAttribute("user",user);
        return "users";
    }
}
