package com.newstock.post.repository;

import com.newstock.post.domain.news.NewsComment;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class NewsCommentRepository {

    private final EntityManager em;

    public List<NewsComment> findById(Long newsId) {
        return em.createQuery("select n from NewsComment n where n.news.newsId = :news")
                .setParameter("news", newsId)
                .getResultList();
    }

    public void save(NewsComment newsComment){
        em.persist(newsComment);
    }
}
