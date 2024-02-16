package com.newstock.post.domain.news;

import com.newstock.post.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class DislikeNews {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dislikeNewsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id")
    private News news;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static DislikeNews makeDislikeNews(News news, User user){
        DislikeNews dislikeNews = new DislikeNews();
        dislikeNews.news = news;
        dislikeNews.user = user;
        return dislikeNews;
    }
}
