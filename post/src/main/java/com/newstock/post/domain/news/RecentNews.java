package com.newstock.post.domain.news;

import com.newstock.post.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class RecentNews {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recentNewsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id")
    private News news;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    private LocalDateTime recentNewsDate;

    public static RecentNews makeRecentNews(News news, User user){
        RecentNews recentNews = new RecentNews();
        recentNews.news = news;
        recentNews.user = user;
        recentNews.recentNewsDate = LocalDateTime.now();
        return recentNews;
    }

    public void setRecentNewsDate(){
        this.recentNewsDate = LocalDateTime.now();
    }
}
