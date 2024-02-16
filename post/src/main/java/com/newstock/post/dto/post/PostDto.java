package com.newstock.post.dto.post;

import com.newstock.post.domain.Category;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PostDto {
    private String title;
    private String postContent;
    private Category category;
    private List<String> postImageList;

    public PostDto(String title, String postContent, Category category) {
        this.title = title;
        this.postContent = postContent;
        this.category = category;
    }
}
