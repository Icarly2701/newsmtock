package com.newstock.post.dto;

import lombok.Data;

import java.util.List;

@Data
public class PostDto {
    private String title;
    private String postContent;
    private String category;
    private List<String> postImageList;

}
