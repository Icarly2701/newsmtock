package com.newstock.post.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class test {

    @GetMapping("/post")
    public String posts(){
        return "postnewspage";
    }
    @GetMapping("/news")
    public String news(){
        return "postnewspage";
    }
}
