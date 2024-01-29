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
        return  em.createQuery("select n from News n order by n.newsDate desc")
                .setMaxResults(10)
                .getResultList();
    }

    public List<News> findByName(String name){
        return em.createQuery("select m from Member m where m.name = :name", News.class)
                .setParameter("name", name)
                .getResultList();
    }

    public News findById(Long newsId) {
        return em.find(News.class, newsId);
    }
}
