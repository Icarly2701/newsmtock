package com.newstock.post.controller;

import com.newstock.post.domain.news.News;
import com.newstock.post.domain.user.User;
import com.newstock.post.dto.news.HomeDto;
import com.newstock.post.dto.news.NewsDto;
import com.newstock.post.service.CustomUserDetails;
import com.newstock.post.service.NewsService;
import com.newstock.post.service.UserService;
import com.newstock.post.web.Login;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.http.HttpRequest;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

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
    public String home(@Login User user, Model model){
        HomeDto homeDto = new HomeDto(newsToNewsDto(newsService.getRecentNewsAboutStock()),
                newsToNewsDto(newsService.getPopularNews()));
        if(user != null){
            homeDto.setPreferenceNews(
                    newsToNewsDto(
                        newsService.getUserPreferenceNews(
                                userService.findUserPreferenceTitle(user.getUserId()
                                )
                        )
                    )
            );
        }else {
            homeDto.setNasdaqList(newsToNewsDto(newsService.getRecentNewsAboutNasdaq()));
        }

        model.addAttribute("homeDto", homeDto);
        return "mainpage";
    }

    private List<NewsDto> newsToNewsDto(List<News> newsList){
        return newsList.stream()
                .map(NewsDto::new)
                .collect(Collectors.toList());
    }
}
