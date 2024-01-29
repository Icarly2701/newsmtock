package com.newstock.post.repository;

import com.newstock.post.domain.news.NewsContent;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class NewsContentRepository {

    private final EntityManager em;
    public void save(NewsContent newsContent){
        em.persist(newsContent);
    }
}
