package com.newstock.post.dto.news;

import com.newstock.post.domain.news.News;
import com.newstock.post.domain.news.NewsComment;
import com.newstock.post.domain.user.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class NewsDetailDto {
    private Long newsId;
    private String newsHeadLine;
    private String newsContent;
    private String newsURL;
    private LocalDateTime newsDate;
    private int newsCheckCount;
    private int newsLikeCount;
    private String newsTopic;

    private List<NewsCommentDto> newsCommentList;

    public NewsDetailDto(News news, List<NewsCommentDto> newsCommentList) {
        this.newsId = news.getNewsId();
        this.newsHeadLine = news.getNewsHeadline();
        this.newsURL = news.getNewsURL();
        this.newsDate = news.getNewsDate();
        this.newsCheckCount = news.getNewsCheckCount();
        this.newsLikeCount = news.getNewsLikeCount();
        this.newsTopic = news.getNewsTopic();
        this.newsCommentList = newsCommentList;

        if(news.getNewsContent() != null)
            this.newsContent = news.getNewsContent().getNewsContentText();

    }
}
