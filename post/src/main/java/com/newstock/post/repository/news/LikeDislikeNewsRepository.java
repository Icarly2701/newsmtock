package com.newstock.post.repository.news;

import com.newstock.post.domain.news.DislikeNews;
import com.newstock.post.domain.news.LikeNews;
import com.newstock.post.domain.news.News;
import com.newstock.post.domain.user.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class LikeDislikeNewsRepository {

    private final EntityManager em;

    public void saveLikeNews(LikeNews likeNews){
        em.persist(likeNews);
    }

    public void saveDislikeNews(DislikeNews dislikeNews){
        em.persist(dislikeNews);
    }

    public void deleteLikeNews(LikeNews likeNews){
        em.remove(likeNews);
    }

    public void deleteDislikeNews(DislikeNews dislikeNews){
        em.remove(dislikeNews);
    }

    public List<LikeNews> findLikeNews(News news, User user){
        return em.createQuery("select r from LikeNews r where r.user.userId = :userId and r.news.newsId = :newsId")
                .setParameter("userId", user.getUserId())
                .setParameter("newsId", news.getNewsId())
                .getResultList();
    }

    public List<DislikeNews> findDislikeNews(News news, User user){
        return em.createQuery("select d from DislikeNews d where d.user.userId = :userId and d.news.newsId = :newsId")
                .setParameter("userId", user.getUserId())
                .setParameter("newsId", news.getNewsId())
                .getResultList();
    }
}
