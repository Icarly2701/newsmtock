package com.newstock.post.domain.post;

import com.newstock.post.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class LikePost {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long likePostId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public static LikePost makeLikePost(Post post, User user){
        LikePost likePost = new LikePost();
        likePost.post = post;
        likePost.user = user;
        return likePost;
    }
}
