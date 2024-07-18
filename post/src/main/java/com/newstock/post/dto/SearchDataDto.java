package com.newstock.post.dto;

import com.newstock.post.domain.news.News;
import com.newstock.post.domain.post.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class SearchDataDto {
    private Long searchId;
    private String searchDataTitle;
    private String searchDataContent;
    private LocalDateTime dateTime;
    private int checkCount;
    private int likeCount;
    private String newsURL;
    private String newsTopic;
    private String postNickname;

    public SearchDataDto(Post post){
        this.searchId = post.getPostId();
        this.searchDataTitle = post.getPostTitle();
        this.dateTime = post.getPostDate();
        this.checkCount = post.getPostCheckCount();
        this.likeCount = post.getPostLikeCount();
        this.postNickname = post.getUser().getNickname();

        if(post.getPostContent() != null)
            this.searchDataContent = post.getPostContent().getPostContentText();
    }
    public SearchDataDto(News news){
        this.searchId = news.getNewsId();
        this.searchDataTitle = news.getNewsHeadline();
        this.dateTime = news.getNewsDate();
        this.checkCount = news.getNewsCheckCount();
        this.likeCount = news.getNewsLikeCount();
        this.newsURL = news.getNewsURL();
        this.newsTopic = news.getNewsTopic();

        if(news.getNewsContent() != null)
            this.searchDataContent = news.getNewsContent().getNewsContentText();
    }
}
