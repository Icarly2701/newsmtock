package com.newstock.post.domain.post;

import com.newstock.post.domain.user.User;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "post_comment")
public class PostComment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postCommentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private LocalDateTime postCommentDate;
    private String postCommentContent;
    private int postCommentLikeCount;

    public static PostComment makePostComment(Post post, User user, String postCommentContent) {
        PostComment postComment = new PostComment();
        postComment.user = user;
        postComment.post = post;
        postComment.postCommentContent = postCommentContent;
        postComment.postCommentLikeCount = 0;
        postComment.postCommentDate = LocalDateTime.now();
        return postComment;
    }
}
