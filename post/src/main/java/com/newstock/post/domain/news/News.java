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
    @OneToOne(fetch = FetchType.LAZY,mappedBy = "news")
    private NewsContent newsContent;

    @Column(unique = true, nullable = false, length = 300)
    private String newsURL;

    @Column(nullable = false, length = 150)
    private String newsHeadline;

    @Column(nullable = false)
    private LocalDateTime newsDate;
    @Column(nullable = false)
    private int newsCheckCount;
    @Column(nullable = false)
    private int newsLikeCount;
    @Column(nullable = false)
    private String newsTopic;

    public static News makeNewsItem(Item item, String topic){
        News news = new News();

        news.newsContent =  NewsContent.makeNewsContent(item.getDescription(), news);
        news.newsHeadline = item.getTitle();
        news.newsURL = item.getLink() != null ? item.getLink() : item.getOriginallink();
        news.newsLikeCount = 0;
        news.newsCheckCount = 0;
        news.newsTopic = topic;
        // 문자열을 LocalDateTime으로 파싱
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        ZonedDateTime zonedDateTime = ZonedDateTime.parse(item.getPubDate(), formatter);

        // LocalDateTime으로 변환
        news.newsDate = zonedDateTime.toLocalDateTime();
        return news;
    }

    public void checkCount(){
        this.newsCheckCount++;
    }
    public void addLike(){
        this.newsLikeCount++;
    }
    public void subLike(){
        this.newsLikeCount--;
    }

    public void setNewsContent(NewsContent newsContent){this.newsContent = newsContent;}
}
