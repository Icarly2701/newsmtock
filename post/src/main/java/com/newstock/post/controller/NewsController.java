package com.newstock.post.controller;

import com.newstock.post.domain.news.News;
import com.newstock.post.service.NewsService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class NewsController {

    private final NewsService newsService;

    @GetMapping("/news/{newsId}")
    public String newsDetail(@PathVariable("newsId") Long newsId, Model model){
        News news = newsService.findById(newsId);
        newsService.checkCountAdd(news);
        model.addAttribute("content", news);
        model.addAttribute("type", "news");
        return "detailpage";
    }
}
