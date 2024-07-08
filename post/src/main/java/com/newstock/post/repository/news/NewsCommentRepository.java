package com.newstock.post.repository.news;

import com.newstock.post.domain.news.NewsComment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NewsCommentRepository extends JpaRepository<NewsComment,Long> {
    List<NewsComment> findByNewsNewsId(Long newsId);
}
