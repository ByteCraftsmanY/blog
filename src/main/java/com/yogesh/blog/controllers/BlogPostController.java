package com.yogesh.blog.controllers;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class BlogPostController {
    @RequestMapping
    String getPage(){
        return "blog-post";
    }
}
