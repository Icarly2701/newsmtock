package com.newstock.post.controller.rest_controller;

import com.newstock.post.domain.user.PreferenceTitle;
import com.newstock.post.domain.user.User;
import com.newstock.post.dto.MypageDto;
import com.newstock.post.service.NewsService;
import com.newstock.post.service.PostService;
import com.newstock.post.service.UserService;
import com.newstock.post.web.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@Slf4j
public class MypageController {

    private final NewsService newsService;
    private final PostService postService;
    private final UserService userService;

    @GetMapping("/test/mypage")
    public MypageDto viewMypage(@Login User user){
        List<PreferenceTitle> userPreferenceTitle = userService.findUserPreferenceTitle(user.getUserId());
        return new MypageDto(
                userPreferenceTitle,
                newsService.getUserPreferenceNews(userPreferenceTitle),
                newsService.getRecentNewsList(user),
                postService.getRecentPostList(user),
                postService.getMyPostList(user));
    }

    @PostMapping("/test/mypage/delete/{preferenceTitleId}")
    public String deletePreferenceTitle(@PathVariable("preferenceTitleId") Long preferenceTitleId){
        userService.deletePreferenceTitle(preferenceTitleId);
        return "ok";
    }

    @PostMapping("/test/mypage/add")
    public String addPreferenceTitle(@Login User user, @RequestParam("interestWord") String interestWord){
        if(!interestWord.trim().isEmpty()){
            userService.addPreferenceTitle(interestWord, user.getUserId());
            newsService.getNewsData(interestWord);
        }
        return "ok";
    }
}
