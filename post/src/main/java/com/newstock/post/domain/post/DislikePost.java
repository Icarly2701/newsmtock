package com.newstock.post.domain.post;

import com.newstock.post.domain.news.News;
import com.newstock.post.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class DislikePost {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long dislikePostId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static DislikePost makeDislikePost(Post post, User user){
        DislikePost dislikePost = new DislikePost();
        dislikePost.post = post;
        dislikePost.user = user;
        return dislikePost;
    }
}
