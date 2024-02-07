package com.newstock.post.controller;

import com.newstock.post.domain.news.News;
import com.newstock.post.domain.news.NewsComment;
import com.newstock.post.domain.news.RecentNews;
import com.newstock.post.domain.user.PreferenceTitle;
import com.newstock.post.domain.user.User;
import com.newstock.post.service.NewsService;
import com.newstock.post.web.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class NewsController {

    private final NewsService newsService;

    @GetMapping("/news/{newsId}")
    public String newsDetail(@Login User user,
                             @PathVariable("newsId") Long newsId,
                             @ModelAttribute("isLike") String isLike,
                             Model model){
        News news = newsService.findById(newsId);
        List<NewsComment> newsCommentList = newsService.findCommentById(newsId);

        if(user != null) {
            RecentNews recentNews = newsService.getRecentNewsAlreadySeen(news, user);
            if(recentNews == null){
                newsService.addRecentNews(news, user);
            }else{
                newsService.updateRecentNews(recentNews);
            }
        }

        if(isLike.isEmpty()) newsService.checkCountAdd(news);

        model.addAttribute("newsCommentList", newsCommentList);
        model.addAttribute("content", news);
        model.addAttribute("type", "news");
        model.addAttribute("viewUser", user);
        return "detailpage";
    }

    @PostMapping("/news/{newsId}")
    public String newsLike(@Login User user,
                           @RequestParam String action,
                           @PathVariable("newsId") Long newsId,
                           RedirectAttributes redirectAttributes){

        News news = newsService.findById(newsId);

        if(action.equals("like")){
            newsService.addLikeCount(news, user);
        }else if(action.equals("dislike")){
            newsService.subLikeCount(news, user);
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

    @PostMapping("/news/{newsId}/delete")
    public String newDeleteComment(@PathVariable("newsId") Long newsId,
                                   @RequestParam("newsCommentId") Long newsCommentId,
                                   RedirectAttributes redirectAttributes){
        newsService.deleteNewsComment(newsCommentId);
        redirectAttributes.addFlashAttribute("isLike", "notCount");
        return "redirect:/news/" + newsId;
    }

}
