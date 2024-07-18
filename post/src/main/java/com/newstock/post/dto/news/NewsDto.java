package com.newstock.post.dto.news;

import com.newstock.post.domain.news.News;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class NewsDto {
    private Long newsId;
    private String newsHeadline;
    private int newsCheckCount;
    private int newsLikeCount;
    private LocalDateTime newsDate;


    public NewsDto(News news) {
        this.newsId = news.getNewsId();
        this.newsHeadline = news.getNewsHeadline();
        this.newsCheckCount = news.getNewsCheckCount();
        this.newsLikeCount = news.getNewsLikeCount();
        this.newsDate = news.getNewsDate();
    }
}
