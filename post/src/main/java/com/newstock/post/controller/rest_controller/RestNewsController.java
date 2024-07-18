package com.newstock.post.controller.rest_controller;

import com.newstock.post.domain.news.News;
import com.newstock.post.domain.user.User;
import com.newstock.post.dto.news.NewsCommentDto;
import com.newstock.post.dto.news.NewsDetailDto;
import com.newstock.post.dto.news.NewsDto;
import com.newstock.post.service.NewsService;
import com.newstock.post.web.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RestNewsController {

    private final NewsService newsService;

    @GetMapping("/test/news/{newsId}")
    public NewsDetailDto viewNewsDetail(@Login User user,
                             @PathVariable("newsId") Long newsId,
                             @ModelAttribute("isLike") String isLike){

        return new NewsDetailDto(
                newsService.processDetailPageNews(newsId, user, isLike),
                newsService.findCommentById(newsId).stream()
                        .map(NewsCommentDto::new)
                        .collect(Collectors.toList()));
    }

    @PostMapping("/test/news/{newsId}")
    public String newsLike(@Login User user,
                           @RequestParam String action,
                           @PathVariable("newsId") Long newsId,
                           RedirectAttributes redirectAttributes){
        newsService.processRecommend(newsId, user, action);
        redirectAttributes.addFlashAttribute("isLike", "notCount");
        return "ok";
    }

    @PostMapping("/test/news/{newsId}/add")
    public String newsAddComment(@Login User user,
                                 @PathVariable("newsId") Long newsId,
                                 @RequestParam("newsCommentContent") String newsCommentContent,
                                 RedirectAttributes redirectAttributes){
        newsService.addNewsComment(newsId, user, newsCommentContent);
        redirectAttributes.addFlashAttribute("isLike", "notCount");
        return "ok";
    }

    @PostMapping("/test/news/{newsId}/delete/comment/{newsCommentId}")
    public String newDeleteComment(@PathVariable("newsId") Long newsId,
                                   @PathVariable("newsCommentId") Long newsCommentId,
                                   RedirectAttributes redirectAttributes){
        newsService.deleteNewsComment(newsCommentId);
        redirectAttributes.addFlashAttribute("isLike", "notCount");
        return "ok";
    }

    @GetMapping("/test/news/economic")
    public List<NewsDto> viewNewsList(@RequestParam(value = "target", required = false) String target){
        return newsService.getEconomicNewsList(target).stream()
                .map(NewsDto::new)
                .collect(Collectors.toList());
    }

    @GetMapping("/test/refresh")
    public String refreshGetTopicNews(@Login User user){
        newsService.getNewsData(user.getUserId());
        return "redirect:/";
    }
}
