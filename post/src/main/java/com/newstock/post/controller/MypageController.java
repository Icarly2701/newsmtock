package com.newstock.post.controller;

import com.newstock.post.domain.user.User;
import com.newstock.post.service.NewsService;
import com.newstock.post.service.UserService;
import com.newstock.post.web.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MypageController {

    private final NewsService newsService;
    private final UserService userService;

    @GetMapping("/mypage")
    public String viewMypage(@Login User user, Model model){
        
    }
}
