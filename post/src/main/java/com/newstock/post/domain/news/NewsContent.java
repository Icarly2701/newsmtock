package com.newstock.post.domain.news;

import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class NewsContent {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long newsContentId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id")
    private News news;

    private String newsContentText;

    public static NewsContent makeNewsContent(String newsContentText){
        NewsContent newsContent = new NewsContent();
        newsContent.newsContentText = newsContentText;
        return newsContent;
    }
}
