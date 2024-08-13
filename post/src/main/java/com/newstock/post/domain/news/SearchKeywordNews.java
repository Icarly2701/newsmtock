package com.newstock.post.domain.news;

import com.newstock.post.api.Item;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Entity
@Getter
public class SearchKeywordNews {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long newsId;

    @Column(unique = true, nullable = false, length = 300)
    private String newsURL;

    @Column(nullable = false, length = 150)
    private String newsHeadLine;

    @Column(nullable = false)
    private LocalDateTime newsDate;

    @Column(nullable = false)
    private String newsContent;


    public static SearchKeywordNews makeSearchKeywordNews(Item item){
        SearchKeywordNews searchKeywordNews = new SearchKeywordNews();
        searchKeywordNews.newsContent = item.getDescription();
        searchKeywordNews.newsHeadLine = item.getTitle();
        searchKeywordNews.newsURL = item.getLink() != null ? item.getLink() : item.getOriginallink();

        // 문자열을 LocalDateTime으로 파싱
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(item.getPubDate(), formatter);

        // LocalDateTime으로 변환
        searchKeywordNews.newsDate = zonedDateTime.toLocalDateTime();

        return searchKeywordNews;
    }
}

