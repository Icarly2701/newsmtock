package com.newstock.post.domain.news;

import com.newstock.post.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class NewsComment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long newsCommentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id", nullable = false)
    private News news;

    @Column(nullable = false)
    private LocalDateTime newsCommentDate;
    @Column(nullable = false)
    private String newsCommentContent;
    @Column(nullable = false)
    private int newsCommentLikeCount;

    public static NewsComment makeNewsCommentItem(News news, User user, String newsCommentContent) {
        NewsComment newsComment = new NewsComment();
        newsComment.user = user;
        newsComment.news = news;
        newsComment.newsCommentContent = newsCommentContent;
        newsComment.newsCommentLikeCount = 0;
        newsComment.newsCommentDate = LocalDateTime.now();
        return newsComment;
    }
}
