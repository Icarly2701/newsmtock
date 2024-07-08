package com.newstock.post.repository.news.newsrep;

import com.newstock.post.domain.news.News;
import com.newstock.post.repository.news.newsrep.CustomNewsRepository;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long>, CustomNewsRepository {
    List<News> findByNewsTopicOrderByNewsDateDesc(String newsTopic);
    List<News> findByNewsOrderByNewsCheckCount();
}
