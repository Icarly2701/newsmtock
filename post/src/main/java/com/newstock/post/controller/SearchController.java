package com.newstock.post.controller;

import com.newstock.post.domain.Target;
import com.newstock.post.domain.news.News;
import com.newstock.post.service.NewsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SearchController {

    private final NewsService newsService;
    @GetMapping("/search")
    public String searchDetail(@RequestParam("keyword") String keyword,
                               @RequestParam("type") String type,
                               @RequestParam(value = "target", required = false) Target target,
                               Model model){

        if(type.equals("news") && !keyword.trim().isEmpty()){
            newsService.getNewsData(keyword);
            List<News> newsData;
            if(target != null){
                newsData = switch (target) {
                    case title_content -> newsService.getRecentNewsAboutTopic(keyword);
                    case content -> newsService.getRecentNewsAboutTitle(keyword);
                    default -> newsService.getRecentNewsAboutContent(keyword);
                };
            }else{
                newsData = newsService.getRecentNewsAboutTopic(keyword);
            }
            model.addAttribute("searchData", newsData);
        }

        return "searchpage";
    }
}
