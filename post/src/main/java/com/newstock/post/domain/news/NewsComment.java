package com.newstock.post.domain.news;

import com.newstock.post.domain.post.Post;
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
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_id")
    private News news;

    private LocalDateTime newsCommentDate;
    private String newsCommentContent;
    private int newsCommentLikeCount;
}
