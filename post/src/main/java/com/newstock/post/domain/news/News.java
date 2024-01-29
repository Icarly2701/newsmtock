package com.newstock.post.domain.news;

import com.newstock.post.api.Item;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Locale;

@Entity
@Getter
@Slf4j
public class News {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long newsId;

    @Cascade(CascadeType.PERSIST)
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "news")
    private NewsContent newsContent;

    private String newsHeadline;
    private String newsURL;
    private LocalDateTime newsDate;
    private int newsCheckCount;

    public static News makeNewsItem(Item item){
        log.info("item = {}", item);
        News news = new News();

        news.newsContent =  NewsContent.makeNewsContent(item.getDescription());
        news.newsHeadline = item.getTitle();
        news.newsURL = item.getLink() != null ? item.getLink() : item.getOriginallink();

        // 문자열을 LocalDateTime으로 파싱
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(item.getPubDate(), formatter);

        // LocalDateTime으로 변환
        news.newsDate = zonedDateTime.toLocalDateTime();
        return news;
    }

    public static void checkCount(){
        //newsCheckCount++;
    }

}
