package com.newstock.post.controller;

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

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final NewsService newsService;
    private final UserService userService;

    @GetMapping("/")
    public String home(@Login User user, Model model){
        HomeDto homeDto = new HomeDto(newsService.getRecentNewsAboutStock(),
                newsService.getPopularNews());
        if(user != null){
            homeDto.setPreferenceNews(newsService.getUserPreferenceNews(userService.findUserPreferenceTitle(user.getUserId())));
        }else{
            homeDto.setNasdaqList(newsService.getRecentNewsAboutNasdaq());
        }
        model.addAttribute("homeDto", homeDto);
        return "mainpage";
    }
}
