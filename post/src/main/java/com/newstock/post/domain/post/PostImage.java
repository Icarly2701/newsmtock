package com.newstock.post.domain.post;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class PostImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_content_id")
    private PostContent postContent;

    private String postImagePath;
}
