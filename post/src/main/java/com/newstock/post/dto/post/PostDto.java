package com.newstock.post.dto.post;

import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.util.List;

@Data
public class PostDto {
    private String title;
    private String postContent;
    private String category;
    private List<String> postImageList;

}
