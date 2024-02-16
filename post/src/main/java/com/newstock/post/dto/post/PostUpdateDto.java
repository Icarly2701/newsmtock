package com.newstock.post.dto.post;

import com.newstock.post.domain.post.Post;
import com.newstock.post.domain.post.PostImage;
import lombok.Data;

import java.util.List;

@Data
public class PostUpdateDto {
    private Post post;
    private List<PostImage> postImageList;

    public PostUpdateDto(Post post, List<PostImage> postImageList) {
        this.post = post;
        this.postImageList = postImageList;
    }
}
