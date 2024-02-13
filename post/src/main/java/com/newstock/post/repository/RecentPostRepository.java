package com.newstock.post.repository;

import com.newstock.post.domain.news.News;
import com.newstock.post.domain.news.RecentNews;
import com.newstock.post.domain.post.Post;
import com.newstock.post.domain.post.RecentPost;
import com.newstock.post.domain.user.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RecentPostRepository {

    private final EntityManager em;

    public void save(RecentPost recentPost){
        em.persist(recentPost);
    }

    public List<RecentPost> getRecentPostAlreadySeen(Post post, User user) {
        return em.createQuery("select r from RecentPost r where r.user.userId = :userId and r.post.postId = :postId")
                .setParameter("userId", user.getUserId())
                .setParameter("newsId", post.getPostId())
                .getResultList();
    }

    public List<RecentPost> getRecentPost(User user) {
        return em.createQuery("select r from RecentPost r where r.user.userId = :userId order by r.recentPostDate desc")
                .setParameter("userId", user.getUserId())
                .getResultList();
    }
}
