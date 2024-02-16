package com.newstock.post.domain.post;

import com.newstock.post.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class RecentPost {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long recentPostKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(nullable = false)
    private LocalDateTime recentPostDate;

    public static RecentPost makeRecentPost(Post post, User user){
        RecentPost recentPost = new RecentPost();
        recentPost.post = post;
        recentPost.user = user;
        recentPost.recentPostDate = LocalDateTime.now();
        return recentPost;
    }

    public void setRecentPostDate(){
        this.recentPostDate = LocalDateTime.now();
    }
}
