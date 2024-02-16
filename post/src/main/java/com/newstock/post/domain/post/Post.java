package com.newstock.post.domain.post;

import com.newstock.post.domain.Category;
import com.newstock.post.domain.user.User;
import com.newstock.post.dto.post.PostDto;
import com.newstock.post.dto.post.PostUploadUpdate;
import jakarta.persistence.*;
import lombok.Getter;
import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;

import java.time.LocalDateTime;

@Entity
@Getter
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    @Cascade(CascadeType.PERSIST)
    private Category category;

    @Cascade({CascadeType.PERSIST, CascadeType.REMOVE})
    @OneToOne(fetch = FetchType.LAZY, mappedBy = "post")
    private PostContent postContent;

    @Column(nullable = false)
    private LocalDateTime postDate;
    @Column(nullable = false)
    private int postCheckCount;
    @Column(nullable = false)
    private int postLikeCount;
    @Column(nullable = false, length = 100)
    private String postTitle;

    public static Post makePost(PostDto postDto, User user){
        Post post = new Post();
        post.user = user;
        post.category = postDto.getCategory();
        post.postContent = PostContent.makePostContent(postDto, post);
        post.postDate = LocalDateTime.now();
        post.postCheckCount = 0;
        post.postLikeCount = 0;
        post.postTitle  = postDto.getTitle();
        return post;
    }

    public void updatePost(PostUploadUpdate postUpload){
        this.postTitle = postUpload.getTitle();
        this.postContent.setPostContent(postUpload.getContent());
    }

    public void checkCount() {
        this.postCheckCount++;
    }

    public void addLike() {
        this.postLikeCount++;
    }

    public void subLike(){
        this.postLikeCount--;
    }
}
