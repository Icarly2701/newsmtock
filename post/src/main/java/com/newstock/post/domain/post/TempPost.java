package com.newstock.post.domain.post;

import com.newstock.post.domain.Category;
import com.newstock.post.domain.user.User;
import com.newstock.post.dto.post.PostDto;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class TempPost {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tempPostId;

    private String tempPostTitle;
    private String tempPostText;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    public static TempPost tempPost(PostDto postDto, User user){
        TempPost tempPost = new TempPost();
        tempPost.user = user;
        tempPost.tempPostTitle = postDto.getTitle();
        tempPost.tempPostText = postDto.getPostContent();
        tempPost.category = Category.makeCategory(postDto.getCategory());
        return tempPost;
    }
}
