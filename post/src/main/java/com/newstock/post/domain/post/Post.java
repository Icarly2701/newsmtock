package com.newstock.post.domain.post;

import com.newstock.post.domain.Category;
import com.newstock.post.domain.user.User;
import com.newstock.post.dto.PostDto;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "post")
    private PostContent postContent;

    private LocalDateTime postDate;
    private int postCheckCount;
    private int postLikeCount;
    private String postTitle;

    public static Post makePost(PostDto postDto, User user){
        Post post = new Post();
        post.user = user;
        post.category = Category.makeCategory(postDto.getCategory());
        post.postContent = PostContent.makePostContent(postDto, post);
        post.postDate = LocalDateTime.now();
        post.postCheckCount = 0;
        post.postLikeCount = 0;
        post.postTitle  = postDto.getTitle();
        return post;
    }

}
