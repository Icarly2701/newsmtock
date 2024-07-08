package com.newstock.post.repository.news;

import com.newstock.post.domain.news.LikeNews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LikeNewsRepository extends JpaRepository<LikeNews, Long> {
    List<LikeNews> findByUserUserIdAndNewsNewsId(Long userId, Long newsId);
}
