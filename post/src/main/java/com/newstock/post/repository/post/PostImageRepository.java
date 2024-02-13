package com.newstock.post.repository.post;

import com.newstock.post.domain.post.PostImage;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class PostImageRepository {

    private final EntityManager em;

    public void save(PostImage postImage){
        em.persist(postImage);
    }

    public List<PostImage> findByPost(Long postId){
        return em.createQuery("select i from PostImage i where i.postContent.post.postId = :postId")
                .setParameter("postId", postId)
                .getResultList();
    }
}
