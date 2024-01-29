package com.newstock.post.controller;

import com.newstock.post.domain.news.News;
import com.newstock.post.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class HomeController {

    private final NewsService newsService;

    @GetMapping("/")
    public String home(Model model){
        List<News> recentNewsAboutStock = newsService.getRecentNewsAboutStock();
        model.addAttribute("newsList",recentNewsAboutStock);
        return "mainpage";
    }
}
