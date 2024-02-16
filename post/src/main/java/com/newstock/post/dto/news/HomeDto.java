package com.newstock.post.dto.news;

import com.newstock.post.domain.news.News;
import lombok.Data;

import java.util.List;

@Data
public class HomeDto {
    private List<News> newsList;
    private List<News> popularNewsList;
    private List<News> preferenceNews;
    private List<News> nasdaqList;

    public HomeDto(List<News> newsList, List<News> popularNewsList) {
        this.newsList = newsList;
        this.popularNewsList = popularNewsList;
    }
}
