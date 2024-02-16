package com.newstock.post.repository.post;

import com.newstock.post.domain.news.NewsComment;
import com.newstock.post.domain.post.Post;
import com.newstock.post.domain.post.PostComment;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PostCommentRepository {

    private final EntityManager em;

    public List<PostComment> findByPost(Long postId){
        return em.createQuery("select p from PostComment p where p.post.postId = :postId", PostComment.class)
                .setParameter("postId", postId)
                .getResultList();
    }

    public void save(PostComment postComment) {
        em.persist(postComment);
    }

    public PostComment findById(Long postCommentId) {
        return em.find(PostComment.class, postCommentId);
    }

    public void deletePostComment(PostComment postComment) {
        em.remove(postComment);
    }

    public void deleteByPost(Long postId) {
        em.createQuery("delete from PostComment pc where pc.post.postId = :postId")
                .setParameter("postId", postId)
                .executeUpdate();
    }
}
