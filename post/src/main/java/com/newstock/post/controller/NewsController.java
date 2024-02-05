package com.newstock.post.controller;

import com.newstock.post.domain.news.News;
import com.newstock.post.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequiredArgsConstructor
@Slf4j
public class NewsController {

    private final NewsService newsService;

    @GetMapping("/news/{newsId}")
    public String newsDetail(@PathVariable("newsId") Long newsId,
                             @ModelAttribute("isLike") String isLike,
                             Model model){
        News news = newsService.findById(newsId);
        if(isLike.isEmpty()) newsService.checkCountAdd(news);

        model.addAttribute("content", news);
        model.addAttribute("type", "news");
        return "detailpage";
    }

    @PostMapping("/news/{newsId}")
    public String newsLike(@RequestParam String action,
                           @PathVariable("newsId") Long newsId,
                           RedirectAttributes redirectAttributes,
                           Model model){

        News news = newsService.findById(newsId);
        log.info("aciont = {}", action);
        if(action.equals("like")){
            newsService.addLikeCount(news);
        }else if(action.equals("dislike")){
            newsService.subLikeCount(news);
        }

        redirectAttributes.addFlashAttribute("isLike", "notCount");
        model.addAttribute("content", news);
        model.addAttribute("type", "news");
        return "redirect:/news/" + newsId;
    }
}
