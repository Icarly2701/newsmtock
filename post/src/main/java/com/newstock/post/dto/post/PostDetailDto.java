package com.newstock.post.dto.post;

import com.newstock.post.domain.post.Post;
import com.newstock.post.domain.user.User;
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
    private User user;
    private Long postId;

    public static PostDetailDto makePostDetailDto(Post post, User user){
        PostDetailDto postDetailDto = new PostDetailDto();
        postDetailDto.date = post.getPostDate();
        postDetailDto.title = post.getPostTitle();
        postDetailDto.nickname = post.getUser().getNickname();
        postDetailDto.checkCount = post.getPostCheckCount();
        postDetailDto.likeCount = post.getPostLikeCount();
        postDetailDto.content = post.getPostContent().getPostContentText();
        postDetailDto.user = user;
        postDetailDto.postId = post.getPostId();
        return postDetailDto;
    }
}
