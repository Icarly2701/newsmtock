package com.newstock.post.repository.news.newsrep;

import com.newstock.post.domain.news.News;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class CustomNewsRepositoryImpl implements CustomNewsRepository{

    private final EntityManager em;

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
}
