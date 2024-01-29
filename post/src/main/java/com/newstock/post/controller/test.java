package com.newstock.post.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class test {

    @GetMapping("/login")
    public String login(){
        return "loginpage";
    }
    @GetMapping("/signup")
    public String signup(){
        return "signuppage";
    }
    @GetMapping("/mypage")
    public String mypage(){
        return "mypage";
    }
    @GetMapping("/post")
    public String posts(){
        return "postnewspage";
    }
    @GetMapping("/news")
    public String news(){
        return "postnewspage";
    }
    @PostMapping("/search")
    public String search(){return "searchapage";}
}
