package com.newstock.post.repository.news;

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

    public List<News> findRecentNewsAboutStock(){
        return em.createQuery("select n from News n where n.newsTopic = :topic order by n.newsDate desc", News.class)
                .setParameter("topic", "코스피")
                .setMaxResults(10)
                .getResultList();
    }
    
    public List<News> findRecentNewsAboutNasdaq() {
        return em.createQuery("SELECT n FROM News n WHERE n.newsTopic = :topic ORDER BY n.newsDate DESC", News.class)
                .setParameter("topic", "나스닥")
                .setMaxResults(10)
                .getResultList();
    }

    public List<News> findRecentNewsAboutTopic(String topic) {
        return em.createQuery("SELECT n FROM News n JOIN n.newsContent nc WHERE n.newsHeadline LIKE :headline or nc.newsContentText LIKE :contentText or n.newsTopic LIKE :topic ORDER BY n.newsDate DESC", News.class)
                .setParameter("headline", "%" + topic + "%")
                .setParameter("contentText", "%" + topic + "%")
                .setParameter("topic", "%" + topic + "%")
                .setMaxResults(10)
                .getResultList();
    }

    public List<News> findRecentNewsAboutTopicTitle(String topic) {
        return em.createQuery("SELECT n FROM News n JOIN n.newsContent nc WHERE n.newsHeadline LIKE :headline ORDER BY n.newsDate DESC", News.class)
                .setParameter("headline", "%" + topic + "%")
                .setMaxResults(10)
                .getResultList();
    }

    public List<News> findRecentNewsAboutTopicContent(String topic) {
        return em.createQuery("SELECT n FROM News n JOIN n.newsContent nc WHERE nc.newsContentText LIKE :contentText ORDER BY n.newsDate DESC", News.class)
                .setParameter("contentText", "%" + topic + "%")
                .setMaxResults(10)
                .getResultList();
    }

    public List<News> findAllNewsAboutTopic(){
        return em.createQuery("select n from News n", News.class)
                .getResultList();
    }

    public News findById(Long newsId) {
        return em.find(News.class, newsId);
    }

    public List<News> findPopularNews() {
        return em.createQuery("select n from News n order by n.newsCheckCount desc", News.class)
                .setMaxResults(10)
                .getResultList();
    }
}
