package com.newstock.post.repository;

import com.newstock.post.domain.news.News;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class NewsRepository {

    private final EntityManager em;

    public void save(News news){
        em.persist(news);
    }

    public News findOne(Long id){
        return em.find(News.class, id);
    }

    public List<News> findAll(){
        return em.createQuery("select m from Member m", News.class)
                .getResultList();
    }

    public List<News> findRecentNewsAboutStock(){
        return em.createQuery("select n from News n where n.newsTopic = :topic order by n.newsDate desc")
                .setParameter("topic", "코스피")
                .setMaxResults(10)
                .getResultList();
    }
    
    public List<News> findRecentNewsAboutNasdaq() {
        return em.createQuery("select n from News n where n.newsTopic = :topic order by n.newsDate desc")
                .setParameter("topic", "나스닥")
                .setMaxResults(10)
                .getResultList();
    }

    public List<News> findRecentNewsAboutTopic(String topic) {
        return em.createQuery("SELECT n FROM News n JOIN n.newsContent nc WHERE n.newsHeadline LIKE :headline OR nc.newsContentText LIKE :contentText OR n.newsTopic = :topic ORDER BY n.newsDate DESC", News.class)
                .setParameter("topic", topic)
                .setParameter("headline", "%" + topic + "%")
                .setParameter("contentText", "%" + topic + "%")
                .setMaxResults(10)
                .getResultList();
    }

    public List<News> findAllNewsAboutTopic(){
        return em.createQuery("select n from News n")
                .getResultList();
    }

    public News findById(Long newsId) {
        return em.find(News.class, newsId);
    }

    public List<News> findPopularNews() {
        return em.createQuery("select n from News n order by n.newsCheckCount desc")
                .setMaxResults(10)
                .getResultList();
    }
}
