package com.newstock.post.controller;

import com.newstock.post.domain.user.User;
import com.newstock.post.dto.news.HomeDto;
import com.newstock.post.service.CustomUserDetails;
import com.newstock.post.service.NewsService;
import com.newstock.post.service.UserService;
import com.newstock.post.web.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.http.HttpRequest;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final NewsService newsService;
    private final UserService userService;


    @GetMapping("/healthcheck")
    public String healthCheck(){
        return "ok";
    }

    @GetMapping("/")
    public String home(@Login String userId, Model model){
        log.info("userId = {}", userId);
        User user = userService.findByUserId(userId);
        HomeDto homeDto = new HomeDto(newsService.getRecentNewsAboutStock(), newsService.getPopularNews());
        if(user != null){
            log.info("asdfsadf");
            homeDto.setPreferenceNews(newsService.getUserPreferenceNews(userService.findUserPreferenceTitle(user.getUserId())));
            model.addAttribute("isLogin", true);
        }else {
            homeDto.setNasdaqList(newsService.getRecentNewsAboutNasdaq());
            model.addAttribute("isLogin", false);
        }

        model.addAttribute("homeDto", homeDto);
        return "mainpage";
    }

}
