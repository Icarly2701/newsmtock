package com.newstock.post.dto.post;

import com.newstock.post.domain.post.Post;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostDetailDto {

    private String title;
    private LocalDateTime date;
    private String nickname;
    private int checkCount;
    private int likeCount;
    private String content;

    public static PostDetailDto makePostDetailDto(Post post){
        PostDetailDto postDetailDto = new PostDetailDto();
        postDetailDto.date = post.getPostDate();
        postDetailDto.title = post.getPostTitle();
        postDetailDto.nickname = post.getUser().getNickname();
        postDetailDto.checkCount = post.getPostCheckCount();
        postDetailDto.likeCount = post.getPostLikeCount();
        postDetailDto.content = post.getPostContent().getPostContentText();
        return postDetailDto;
    }
}
