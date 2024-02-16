package com.newstock.post.domain.post;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class PostImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_content_id", nullable = false)
    private PostContent postContent;

    @Column(nullable = false, length = 600)
    private String postImagePath;

    public static PostImage makePostImage(String postImagePath, PostContent postContent){
        PostImage postImage = new PostImage();
        postImage.postContent = postContent;
        postImage.postImagePath = postImagePath;
        return postImage;
    }
}
