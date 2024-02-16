package com.newstock.post.domain.news;

import jakarta.persistence.*;
import lombok.Getter;
@Entity
@Getter
public class NewsContent {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long newsContentId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id", nullable = false)
    private News news;

    @Column(nullable = false, length = 1000)
    private String newsContentText;

    public static NewsContent makeNewsContent(String newsContentText, News news){
        NewsContent newsContent = new NewsContent();
        newsContent.newsContentText = newsContentText;
        newsContent.news = news;
        return newsContent;
    }
}
