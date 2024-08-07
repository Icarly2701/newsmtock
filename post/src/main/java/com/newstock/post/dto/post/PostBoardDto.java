package com.newstock.post.dto.post;

import com.newstock.post.domain.post.Post;
import lombok.Data;

import java.util.List;

@Data
public class PostBoardDto {
    private List<PostTitleDto> postList;
    private String mainName;
    private String category;

    public PostBoardDto(List<PostTitleDto> postList, String mainName, String category) {
        this.postList = postList;
        this.mainName = mainName;
        this.category = category;
    }
}
