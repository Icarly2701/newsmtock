package com.newstock.post.repository.news.newsrep;

import com.newstock.post.domain.news.News;

import java.util.List;

public interface CustomNewsRepository {
    List<News> findRecentNewsAboutTopic(String topic);
    List<News> findRecentNewsAboutTopicTitle(String topic);
    List<News> findRecentNewsAboutTopicContent(String topic);
}
