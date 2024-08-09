package com.newstock.post.repository.news;

import com.newstock.post.domain.news.NewsContent;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NewsContentRepository extends JpaRepository<NewsContent, Long> {
}
