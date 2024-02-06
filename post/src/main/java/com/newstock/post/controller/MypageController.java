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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class MypageController {

    private final NewsService newsService;
    private final UserService userService;

    @GetMapping("/mypage")
    public String viewMypage(@Login User user, Model model){
        List<PreferenceTitle> userPreferenceTitle = userService.findUserPreferenceTitle(user.getUserId());
        model.addAttribute("preferenceTitle", userPreferenceTitle);

        List<News> aboutPreferenceTitle = new ArrayList<>();
        for(PreferenceTitle preferenceTitle: userPreferenceTitle){
            aboutPreferenceTitle.addAll(newsService.getRecentNewsAboutTopic(preferenceTitle.getPreferenceTitle()));
        }
        model.addAttribute("preferenceNews", aboutPreferenceTitle);
        return "mypage";
    }

    @PostMapping("/mypage/delete/{preferenceTitleId}")
    public String deletePreferenceTitle(@PathVariable("preferenceTitleId") Long preferenceTitleId){
        userService.deletePreferenceTitle(preferenceTitleId);
        return "redirect:/mypage";
    }
}
