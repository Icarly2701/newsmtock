package com.newstock.post.domain.post;

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

    @OneToMany(mappedBy = "postContent")
    private List<PostImage> postImageList = new ArrayList<>();

    private String postContentText;
}
