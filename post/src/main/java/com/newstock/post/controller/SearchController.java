package com.newstock.post.controller;

import com.newstock.post.domain.Target;
import com.newstock.post.service.NewsService;
import com.newstock.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SearchController {

    private final NewsService newsService;
    private final PostService postService;
    @GetMapping("/search")
    public String searchDetail(@RequestParam("keyword") String keyword,
                               @RequestParam("type") String type,
                               @RequestParam(value = "target", required = false) Target target,
                               Model model){
        if(keyword.trim().isEmpty()) return "searchpage";
        model.addAttribute("searchData", type.equals("news") ? newsService.getSearchData(keyword, target)
                : postService.getSearchData(keyword, target));
        return type.equals("news") ? "searchpage" : "searchpostpage";
    }
}
