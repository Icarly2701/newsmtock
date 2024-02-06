package com.newstock.post.controller;

import com.newstock.post.domain.news.News;
import com.newstock.post.domain.news.NewsComment;
import com.newstock.post.domain.user.User;
import com.newstock.post.service.NewsService;
import com.newstock.post.web.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

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
        List<NewsComment> newsCommentList = newsService.findCommentById(newsId);

        if(isLike.isEmpty()) newsService.checkCountAdd(news);

        model.addAttribute("newsCommentList", newsCommentList);
        model.addAttribute("content", news);
        model.addAttribute("type", "news");
        return "detailpage";
    }

    @PostMapping("/news/{newsId}")
    public String newsLike(@RequestParam String action,
                           @PathVariable("newsId") Long newsId,
                           RedirectAttributes redirectAttributes){

        News news = newsService.findById(newsId);

        if(action.equals("like")){
            newsService.addLikeCount(news);
        }else if(action.equals("dislike")){
            newsService.subLikeCount(news);
        }

        redirectAttributes.addFlashAttribute("isLike", "notCount");
        return "redirect:/news/" + newsId;
    }

    @PostMapping("/news/{newsId}/add")
    public String newsAddComment(@Login User user,
                                 @PathVariable("newsId") Long newsId,
                                 @RequestParam("newsCommentContent") String newsCommentContent,
                                 RedirectAttributes redirectAttributes){
        newsService.addNewsComment(newsId, user, newsCommentContent);
        redirectAttributes.addFlashAttribute("isLike", "notCount");
        return "redirect:/news/" + newsId;
    }
}
