package com.newstock.post.repository.news;

import com.newstock.post.domain.news.SearchKeywordNews;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SearchKeywordNewsRepository extends JpaRepository<SearchKeywordNews, Long> {
}
