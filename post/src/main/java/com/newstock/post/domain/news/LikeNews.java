package com.newstock.post.domain.news;

import com.newstock.post.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class LikeNews {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likeNewsId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id", nullable = false)
    private News news;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public static LikeNews makeLikeNews(News news, User user){
        LikeNews likeNews = new LikeNews();
        likeNews.news = news;
        likeNews.user = user;
        return likeNews;
    }
}
