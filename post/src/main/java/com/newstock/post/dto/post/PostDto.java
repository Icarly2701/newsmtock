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

}
