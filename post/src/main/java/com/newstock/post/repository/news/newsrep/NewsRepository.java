package com.newstock.post.repository.news.newsrep;

import com.newstock.post.domain.news.News;
import com.newstock.post.repository.news.newsrep.CustomNewsRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long>, CustomNewsRepository {
    @Query("SELECT n FROM News n JOIN FETCH n.newsContent WHERE n.newsTopic = :newsTopic ORDER BY n.newsDate DESC")
    List<News> findByNewsTopicOrderByNewsDateDesc(@Param("newsTopic") String newsTopic, Pageable pageable);
    List<News> findAllByOrderByNewsCheckCount(Pageable pageable);
}
