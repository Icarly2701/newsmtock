package com.newstock.post.repository.news.newsrep;

import com.newstock.post.domain.news.News;
import com.newstock.post.repository.news.newsrep.CustomNewsRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NewsRepository extends JpaRepository<News, Long>, CustomNewsRepository {
    @Query("SELECT n FROM News n JOIN FETCH n.newsContent WHERE n.newsTopic = :newsTopic ORDER BY n.newsDate DESC")
    List<News> findByNewsTopicOrderByNewsDateDesc(@Param("newsTopic") String newsTopic, Pageable pageable);
    @Query(value = "INSERT INTO news (newsurl, news_headline, news_date, news_check_count, news_like_count, news_topic) " +
            "VALUES (:newsURL, :newsHeadline, :newsDate, :newsCheckCount, :newsLikeCount, :newsTopic) " +
            "ON DUPLICATE KEY UPDATE " +
            "news_headline = VALUES(news_headline), " +
            "news_date = VALUES(news_date), " +
            "news_check_count = VALUES(news_check_count), " +
            "news_like_count = VALUES(news_like_count), " +
            "news_topic = VALUES(news_topic)", nativeQuery = true)
    void upsertNews(@Param("newsURL") String newsURL,
                    @Param("newsHeadline") String newsHeadline,
                    @Param("newsDate") LocalDateTime newsDate,
                    @Param("newsCheckCount") int newsCheckCount,
                    @Param("newsLikeCount") int newsLikeCount,
                    @Param("newsTopic") String newsTopic);

    News findByNewsURL(String newsURL);
    List<News> findAllByOrderByNewsCheckCount(Pageable pageable);


}
