package com.newstock.post.domain.post;

import com.newstock.post.dto.post.PostDto;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class PostContent {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postContentId;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id", nullable = false)
    private Post post;

    @Column(nullable = false, length = 1000)
    private String postContentText;

    public static PostContent makePostContent(PostDto postDto, Post post){
        PostContent postContent = new PostContent();
        postContent.postContentText = postDto.getPostContent();
        postContent.post = post;
        return postContent;
    }


    public void setPostContent(String content) {
        this.postContentText = content;
    }
}
