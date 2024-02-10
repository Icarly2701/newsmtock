package com.newstock.post.domain.post;

import com.newstock.post.dto.PostDto;
import jakarta.persistence.*;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class PostContent {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postContentId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    private String postContentText;

    public static PostContent makePostContent(PostDto postDto, Post post){
        PostContent postContent = new PostContent();
        postContent.postContentText = postDto.getPostContent();
        postContent.post = post;
        return postContent;
    }
}
