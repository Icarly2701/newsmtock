package com.newstock.post.repository.post;

import com.newstock.post.domain.news.DislikeNews;
import com.newstock.post.domain.news.LikeNews;
import com.newstock.post.domain.news.News;
import com.newstock.post.domain.post.DislikePost;
import com.newstock.post.domain.post.LikePost;
import com.newstock.post.domain.post.Post;
import com.newstock.post.domain.user.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class LikeDislikePostRepository {

    private final EntityManager em;

    public void saveLikePost(LikePost likePost){ em.persist(likePost);}

    public void saveDislikePost(DislikePost dislikePost){ em.persist(dislikePost);}

    public void deleteLikePost(LikePost likePost){ em.remove(likePost);}

    public void deleteDislikePost(DislikePost dislikePost){ em.remove(dislikePost);}

    public List<LikePost> findLikePost(Post post, User user){
        return em.createQuery("select r from LikePost r where r.user.userId = :userId and r.post.postId = :postId")
                .setParameter("userId", user.getUserId())
                .setParameter("postId", post.getPostId())
                .getResultList();
    }

    public List<DislikePost> findDislikePost(Post post, User user){
        return em.createQuery("select d from DislikePost d where d.user.userId = :userId and d.post.postId = :postId")
                .setParameter("userId", user.getUserId())
                .setParameter("postId", post.getPostId())
                .getResultList();
    }
}

