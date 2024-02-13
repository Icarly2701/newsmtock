package com.newstock.post.repository;

import com.newstock.post.domain.news.News;
import com.newstock.post.domain.post.Post;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PostRepository {

    private final EntityManager em;

    public List<Post> findAll(){
        return em.createQuery("select p from Post p", Post.class)
            .getResultList();
    }

    public Long save(Post post){
        em.persist(post);
        return post.getPostId();
    }

    public Post findById(Long postId) {
        return em.find( Post.class, postId);
    }
}
