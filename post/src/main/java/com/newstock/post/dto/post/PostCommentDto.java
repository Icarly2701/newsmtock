package com.newstock.post.dto.post;

import com.newstock.post.domain.post.PostComment;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostCommentDto {

    public String commentNickname;
    public String commentContent;
    public int commentLikeCount;
    public LocalDateTime commentDate;

    public PostCommentDto(PostComment postComment){
        this.commentContent = postComment.getPostCommentContent();
        this.commentNickname = postComment.getUser().getNickname();
        this.commentLikeCount = postComment.getPostCommentLikeCount();
        this.commentDate = postComment.getPostCommentDate();
    }
}
