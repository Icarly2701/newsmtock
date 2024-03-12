package com.newstock.post.controller;

import com.newstock.post.domain.user.User;
import com.newstock.post.dto.news.NewsDetailDto;
import com.newstock.post.repository.user.PreferenceTitleRepository;
import com.newstock.post.service.NewsService;
import com.newstock.post.web.Login;
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
    public String viewNewsDetail(@Login User user,
                             @PathVariable("newsId") Long newsId,
                             @ModelAttribute("isLike") String isLike,
                             Model model){

        model.addAttribute("newsDetail", new NewsDetailDto(
                newsService.processDetailPageNews(newsId, user, isLike),
                newsService.findCommentById(newsId),
                user));
        return "newsdetailpage";
    }

    @PostMapping("/news/{newsId}")
    public String newsLike(@Login User user,
                           @RequestParam String action,
                           @PathVariable("newsId") Long newsId,
                           RedirectAttributes redirectAttributes){
        newsService.processRecommend(newsId, user, action);
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

    @PostMapping("/news/{newsId}/delete/comment/{newsCommentId}")
    public String newDeleteComment(@PathVariable("newsId") Long newsId,
                                   @PathVariable("newsCommentId") Long newsCommentId,
                                   RedirectAttributes redirectAttributes){
        newsService.deleteNewsComment(newsCommentId);
        redirectAttributes.addFlashAttribute("isLike", "notCount");
        return "redirect:/news/" + newsId;
    }

    @GetMapping("/news/economic")
    public String viewNewsList(@RequestParam(value = "target", required = false) String target,
                               Model model){
        model.addAttribute("newsList", newsService.getEconomicNewsList(target));
        return "newspage";
    }

    @GetMapping("/refresh")
    public String refreshGetTopicNews(@Login User user){
        newsService.getNewsData(user.getUserId());
        return "redirect:/";
    }
}
