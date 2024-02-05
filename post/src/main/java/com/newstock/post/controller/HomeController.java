package com.newstock.post.controller;

import com.newstock.post.domain.news.News;
import com.newstock.post.domain.user.PreferenceTitle;
import com.newstock.post.domain.user.User;
import com.newstock.post.service.NewsService;
import com.newstock.post.service.UserService;
import com.newstock.post.web.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class HomeController {

    private final NewsService newsService;
    private final UserService userService;

    @GetMapping("/")
    public String home(@Login User user, Model model){
        List<News> recentNewsAboutStock = newsService.getRecentNewsAboutStock();
        List<News> popularNews = newsService.getPopularNews();

        model.addAttribute("newsList",recentNewsAboutStock);
        model.addAttribute("popularNewsList", popularNews);

        if(user != null){
            List<PreferenceTitle> userPreferenceTitle = userService.findUserPreferenceTitle(user.getUserId());
            List<News> aboutPreferenceTitle = new ArrayList<>();
            for(PreferenceTitle preferenceTitle: userPreferenceTitle){
                aboutPreferenceTitle.addAll(newsService.getRecentNewsAboutTopic(preferenceTitle.getPreferenceTitle()));
            }
            model.addAttribute("preferenceNews", aboutPreferenceTitle);
            return "mainpage";
        }

        List<News> recentNewsAboutNasdaq = newsService.getRecentNewsAboutNasdaq();
        model.addAttribute("nasdaqList", recentNewsAboutNasdaq);
        return "mainpage";
    }
}
