package com.newstock.post.dto.post;

import com.newstock.post.domain.post.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostTitleDto {

    public String title;
    public String nickname;
    public int checkCount;
    public int likeCount;
    public LocalDateTime date;

    public PostTitleDto(Post post){
        this.title = post.getPostTitle();
        this.nickname = post.getUser().getNickname();
        this.checkCount = post.getPostCheckCount();
        this.likeCount = post.getPostLikeCount();
        this.date = post.getPostDate();
    }
}
