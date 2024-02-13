package com.newstock.post.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newstock.post.api.Item;
import com.newstock.post.domain.news.*;
import com.newstock.post.domain.user.User;
import com.newstock.post.repository.news.LikeDislikeNewsRepository;
import com.newstock.post.repository.news.NewsCommentRepository;
import com.newstock.post.repository.news.NewsRepository;
import com.newstock.post.repository.news.RecentNewsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class NewsService {

    private final NewsRepository newsRepository;
    private final NewsCommentRepository newsCommentRepository;
    private final RecentNewsRepository recentNewsRepository;
    private final LikeDislikeNewsRepository likeDislikeNewsRepository;

    public List<News> getRecentNewsAboutStock(){
        return newsRepository.findRecentNewsAboutStock();
    };
    public List<News> getRecentNewsAboutNasdaq() {
        return newsRepository.findRecentNewsAboutNasdaq();
    }

    public List<News> getRecentNewsAboutTopic(String topic){
        return newsRepository.findRecentNewsAboutTopic(topic);
    }
    public List<News> getRecentNewsAboutTitle(String topic){
        return newsRepository.findRecentNewsAboutTopicTitle(topic);
    }
    public List<News> getRecentNewsAboutContent(String topic){
        return newsRepository.findRecentNewsAboutTopicContent(topic);
    }

    public List<News> getPopularNews(){ return newsRepository.findPopularNews();}
    public News findById(Long newsId){
        return newsRepository.findById(newsId);
    }

    @Transactional
    public void checkCountAdd(News news){
        news.checkCount();
    }

    @Transactional
    public void addLikeCount(News news, User user){
        List<LikeNews> likeNews = likeDislikeNewsRepository.findLikeNews(news, user);
        List<DislikeNews> dislikeNews = likeDislikeNewsRepository.findDislikeNews(news, user);

        if(likeNews.isEmpty() && dislikeNews.isEmpty()){
            news.addLike();
            likeDislikeNewsRepository.saveLikeNews(LikeNews.makeLikeNews(news, user));
            return;
        }else if(likeNews.isEmpty()){
            news.addLike();
            likeDislikeNewsRepository.deleteDislikeNews(dislikeNews.get(0));
            return;
        }

        news.subLike();
        likeDislikeNewsRepository.deleteLikeNews(likeNews.get(0));
    }

    @Transactional
    public void subLikeCount(News news, User user){
        List<DislikeNews> dislikeNews = likeDislikeNewsRepository.findDislikeNews(news, user);
        List<LikeNews> likeNews = likeDislikeNewsRepository.findLikeNews(news, user);

        if(dislikeNews.isEmpty() && likeNews.isEmpty()){
            news.subLike();
            likeDislikeNewsRepository.saveDislikeNews(DislikeNews.makeDislikeNews(news, user));
            return;
        }else if(dislikeNews.isEmpty()){
            news.subLike();
            likeDislikeNewsRepository.deleteLikeNews(likeNews.get(0));
            return;
        }

        news.addLike();
        likeDislikeNewsRepository.deleteDislikeNews(dislikeNews.get(0));
    }

    public void getNewsData(String topic){
        getNewsDataUseApi(topic);
    }

    @Scheduled(fixedDelay = 300000000)
    public void getKospiNewsData(){
        getNewsDataUseApi("코스피");
    }
    @Scheduled(fixedDelay = 300000000)
    public void getNasdaqNewsData(){
        getNewsDataUseApi("나스닥");
    }

    private String getApiUrl(String topic) {
        // Open API 호출을 위한 URL
        String baseUrl = "https://openapi.naver.com/v1/search/news.json";

        return UriComponentsBuilder.fromUriString(baseUrl)
                .queryParam("query", topic)
                .queryParam("display", 10)
                .queryParam("sort", "date")
                .build()
                .toString();
    }

    private WebClient getWebClient() {
        String clientId = "xPUQv5Cn2RH7awxJc1re";
        String clientSecret = "JQjpJochy7";

        // WebClient 생성
        return WebClient.builder()
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .defaultHeader(HttpHeaders.USER_AGENT, "Spring WebClient")
                .defaultHeader("X-Naver-Client-Id", clientId)
                .defaultHeader("X-Naver-Client-Secret", clientSecret)
                .build();
    }

    private void getNewsDataUseApi(String topic) {
        WebClient webClient = getWebClient();
        String apiUrl = getApiUrl(topic);

        Map<String, Object> newsData = webClient.get()
                .uri(apiUrl)
                .retrieve()
                .bodyToMono(Map.class)
                .block();

        List<Item> items = (List<Item>) newsData.get("items");
        List<News> compareTopic = newsRepository.findAllNewsAboutTopic();

        for (int i = 0; i < items.size(); i++) {
            ObjectMapper mapper = new ObjectMapper();

            Item item = mapper.convertValue(items.get(i), Item.class);
            item.cleanHtmlCode();

            String newsLink = item.getLink() != null ? item.getLink() : item.getOriginallink();
            Optional<News> existingNews = compareTopic.stream()
                    .filter(v -> ((v.getNewsURL().equals(newsLink))))
                    .findAny();

            if(existingNews.isEmpty()){
                newsRepository.save(News.makeNewsItem(item, topic));
            }
        }
    }

    public List<NewsComment> findCommentById(Long newsId) {
        return newsCommentRepository.findAll(newsId);
    }

    @Transactional
    public void addNewsComment(Long newsId, User user, String newsCommentContent) {
        News news = newsRepository.findById(newsId);
        NewsComment newsComment = NewsComment.makeNewsCommentItem(news, user, newsCommentContent);
        newsCommentRepository.save(newsComment);
    }

    @Transactional
    public void deleteNewsComment(Long newsCommentId) {
        NewsComment newsComment = newsCommentRepository.findById(newsCommentId);
        newsCommentRepository.deleteNewsComment(newsComment);
    }

    @Transactional
    public void addRecentNews(News news, User user) {
        RecentNews recentNews = RecentNews.makeRecentNews(news,user);
        recentNewsRepository.save(recentNews);
    }

    @Transactional
    public void updateRecentNews(RecentNews recentNews){
        recentNews.setRecentNewsDate();
    }

    public RecentNews getRecentNewsAlreadySeen(News news, User user) {
        List<RecentNews> recentNews = recentNewsRepository.getRecentNewsAlreadySeen(news, user);
        if(recentNews.isEmpty()){
            return null;
        }
        return recentNews.get(0);
    }

    public List<RecentNews> getRecentNewsList(User user){
        return recentNewsRepository.getRecentNews(user);
    }
}
