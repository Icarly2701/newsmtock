package com.newstock.post.dto.post;

import com.newstock.post.domain.Category;
import com.newstock.post.domain.post.TempPost;
import lombok.Data;

@Data
public class PostAddDto {
    private String category;
    private TempPost tempData;
    private PostUpload postUpload;

    public PostAddDto(String category, TempPost tempPost, PostUpload postUpload) {
        this.category = category;
        this.tempData = tempPost;
        this.postUpload = postUpload;
    }
}
