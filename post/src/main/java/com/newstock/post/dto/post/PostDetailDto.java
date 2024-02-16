package com.newstock.post.dto.post;

import com.newstock.post.domain.post.Post;
import com.newstock.post.domain.post.PostComment;
import com.newstock.post.domain.post.PostImage;
import com.newstock.post.domain.user.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

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

    private List<PostComment> postCommentList;
    private List<PostImage> postImageList;
    private User viewUser;
    private String category;

    public static PostDetailDto makePostDetailDto(Post post, User user, List<PostComment> postCommentList, List<PostImage> postImageList){
        PostDetailDto postDetailDto = new PostDetailDto();
        postDetailDto.date = post.getPostDate();
        postDetailDto.title = post.getPostTitle();
        postDetailDto.nickname = post.getUser().getNickname();
        postDetailDto.checkCount = post.getPostCheckCount();
        postDetailDto.likeCount = post.getPostLikeCount();
        postDetailDto.content = post.getPostContent().getPostContentText();
        postDetailDto.viewUser = user;
        postDetailDto.postId = post.getPostId();
        postDetailDto.category = post.getCategory().getCategoryContent();
        postDetailDto.user = post.getUser();
        postDetailDto.postCommentList = postCommentList;
        postDetailDto.postImageList = postImageList;
        return postDetailDto;
    }
}
