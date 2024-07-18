package com.newstock.post.controller.rest_controller;

import com.newstock.post.domain.Target;
import com.newstock.post.dto.SearchDataDto;
import com.newstock.post.service.NewsService;
import com.newstock.post.service.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RestSearchController {

    private final NewsService newsService;
    private final PostService postService;
    @GetMapping("/test/search")
    public List<SearchDataDto> searchDetail(@RequestParam("keyword") String keyword,
                                            @RequestParam("type") String type,
                                            @RequestParam(value = "target", required = false) Target target){
        if(keyword.trim().isEmpty()) return new ArrayList<>();

        return type.equals("news") ?
                newsService.getSearchData(keyword, target).stream()
                        .map(SearchDataDto::new).collect(Collectors.toList()) :
                postService.getSearchData(keyword, target).stream()
                        .map(SearchDataDto::new).collect(Collectors.toList());
    }
}
