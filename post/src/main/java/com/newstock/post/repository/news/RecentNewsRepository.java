package com.newstock.post.repository.news;

import com.newstock.post.domain.news.News;
import com.newstock.post.domain.news.RecentNews;
import com.newstock.post.domain.user.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class RecentNewsRepository {

    private final EntityManager em;

    public void save(RecentNews recentNews){
        em.persist(recentNews);
    }

    public List<RecentNews> getRecentNewsAlreadySeen(News news, User user) {
        return em.createQuery("select r from RecentNews r where r.user.userId = :userId and r.news.newsId = :newsId")
                .setParameter("userId", user.getUserId())
                .setParameter("newsId", news.getNewsId())
                .getResultList();
    }

    public List<RecentNews> getRecentNews(User user) {
        return em.createQuery("select r from RecentNews r where r.user.userId = :userId order by r.recentNewsDate desc")
                .setParameter("userId", user.getUserId())
                .getResultList();
    }
}
