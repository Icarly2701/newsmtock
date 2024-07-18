package com.newstock.post.dto.news;

import com.newstock.post.domain.news.News;
import lombok.Data;

import java.util.List;

@Data
public class HomeDto {
    private List<NewsDto> newsList;
    private List<NewsDto> popularNewsList;
    private List<NewsDto> preferenceNews;
    private List<NewsDto> nasdaqList;

    public HomeDto(List<NewsDto> newsList, List<NewsDto> popularNewsList) {
        this.newsList = newsList;
        this.popularNewsList = popularNewsList;
    }
}
