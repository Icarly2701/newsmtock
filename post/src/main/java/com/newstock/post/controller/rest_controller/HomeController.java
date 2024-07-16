package com.newstock.post.controller.rest_controller;

import com.newstock.post.domain.user.User;
import com.newstock.post.dto.news.HomeDto;
import com.newstock.post.service.NewsService;
import com.newstock.post.service.UserService;
import com.newstock.post.web.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final NewsService newsService;
    private final UserService userService;


    @GetMapping("/healthcheck")
    public String healthCheck(){
        return "ok";
    }

    @GetMapping("/test")
    public HomeDto home(@Login User user){
        HomeDto homeDto = new HomeDto(newsService.getRecentNewsAboutStock(), newsService.getPopularNews());
        if(user != null){
            homeDto.setPreferenceNews(newsService.getUserPreferenceNews(userService.findUserPreferenceTitle(user.getUserId())));
        }else {
            homeDto.setNasdaqList(newsService.getRecentNewsAboutNasdaq());
        }
        return homeDto;
    }
}
