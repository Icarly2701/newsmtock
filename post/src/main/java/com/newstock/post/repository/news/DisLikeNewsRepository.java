package com.newstock.post.repository.news;

import com.newstock.post.domain.news.DislikeNews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DisLikeNewsRepository extends JpaRepository<DislikeNews, Long> {
    List<DislikeNews> findByUserUserIdAndNewsNewsId(Long userId, Long newsId);
}
