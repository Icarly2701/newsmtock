package com.newstock.post.controller.rest_controller;

import com.newstock.post.domain.news.News;
import com.newstock.post.domain.user.User;
import com.newstock.post.dto.news.HomeDto;
import com.newstock.post.dto.news.NewsDto;
import com.newstock.post.service.NewsService;
import com.newstock.post.service.UserService;
import com.newstock.post.web.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RestHomeController {

    private final NewsService newsService;
    private final UserService userService;


    @GetMapping("/test")
    public HomeDto home(@Login User user){
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
        return homeDto;
    }

    private List<NewsDto> newsToNewsDto(List<News> newsList){
        return newsList.stream()
                .map(NewsDto::new)
                .collect(Collectors.toList());
    }
}
