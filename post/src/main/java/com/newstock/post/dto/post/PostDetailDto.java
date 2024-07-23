package com.newstock.post.dto.post;

import com.newstock.post.domain.post.Post;
import com.newstock.post.domain.post.PostComment;
import com.newstock.post.domain.post.PostImage;
import com.newstock.post.domain.user.User;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Data
public class PostDetailDto {

    private String title;
    private LocalDateTime date;
    private String nickname;
    private int checkCount;
    private int likeCount;
    private String content;
    private Long postId;

    private List<PostCommentDto> postCommentList;
    private List<PostImageDto> postImageList;
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


        postDetailDto.postCommentList = postCommentList.stream()
                .map(PostCommentDto::new).collect(Collectors.toList());
        postDetailDto.postImageList = postImageList.stream()
                .map(PostImageDto::new).collect(Collectors.toList());
        return postDetailDto;
    }
}
