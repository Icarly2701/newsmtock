package com.newstock.post.dto.post;

import com.newstock.post.domain.post.PostImage;
import lombok.Data;

@Data
public class PostImageDto {

    public String postImagePath;

    public PostImageDto(PostImage postImage){
        this.postImagePath = postImage.getPostImagePath();
    }
}
