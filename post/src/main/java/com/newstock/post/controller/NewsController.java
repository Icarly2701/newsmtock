package com.newstock.post.controller;

import com.newstock.post.domain.news.News;
import com.newstock.post.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping("/news/{newsId}")
    public String newsDetail(@RequestParam("newsId") Long newsId){
        News news = newsService.findById(newsId);
    
        return "detailpage";
    }
}
