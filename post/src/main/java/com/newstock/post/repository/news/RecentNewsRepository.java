package com.newstock.post.repository.news;

import com.newstock.post.domain.news.RecentNews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecentNewsRepository extends JpaRepository<RecentNews, Long> {
    List<RecentNews> findByUserUserIdAndNewsNewsId(Long userId, Long newsId);
    List<RecentNews> findByUserUserIdOrderByRecentNewsDateDesc(Long userId);
}
