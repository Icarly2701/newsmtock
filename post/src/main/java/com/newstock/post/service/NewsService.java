package com.newstock.post.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newstock.post.api.Item;
import com.newstock.post.domain.Target;
import com.newstock.post.domain.news.*;
import com.newstock.post.domain.user.PreferenceTitle;
import com.newstock.post.domain.user.User;
import com.newstock.post.repository.news.*;
import com.newstock.post.repository.news.newsrep.NewsRepository;
import com.newstock.post.repository.user.PreferenceTitleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class NewsService {

    private final NewsRepository newsRepository;
    private final RecentNewsRepository recentNewsRepository;
    private final LikeNewsRepository likeNewsRepository;
    private final DisLikeNewsRepository disLikeNewsRepository;
    private final PreferenceTitleRepository preferenceTitleRepository;
    private final NewsCommentRepository newsCommentRepository;

    public void getNewsData(String topic){
        getNewsDataUseApi(topic);
    }

    public void getNewsData(Long userId){
        preferenceTitleRepository.findByUserUserId(userId)
                .forEach(v -> getNewsDataUseApi(v.getPreferenceTitle()));
    }

    public void saveNews(Item item, String topic){
        if(topic.equals("코스피") || topic.equals("나스닥")){
            newsRepository.save(News.makeNewsItem(item, topic));
            return;
        }

        // 그 외의 뉴스는 redis로 저장 후 보여줌
    }

    public List<News> getPopularNews(){
        return newsRepository.findByNewsOrderByNewsCheckCount();
//        return newsRepository.findPopularNews();
    }

    public List<News> getUserPreferenceNews(List<PreferenceTitle> userPreferenceTitle) {
        return userPreferenceTitle.stream()
                .map(preferenceTitle -> this.getRecentNewsAboutTopic(preferenceTitle.getPreferenceTitle()))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public List<News> getSearchData(String keyword, Target target) {
        this.getNewsData(keyword);
        return target == null ? this.getRecentNewsAboutTopic(keyword) : switch (target) {
            case title_content -> this.getRecentNewsAboutTopic(keyword);
            case content -> this.getRecentNewsAboutTitle(keyword);
            default -> this.getRecentNewsAboutContent(keyword);
        };
    }

    public List<News> getEconomicNewsList(String target) {
        return Stream.concat(this.getRecentNewsAboutNasdaq().stream(), this.getRecentNewsAboutStock().stream())
                .sorted(target == null ? Comparator.comparing(News::getNewsDate) : switch (target) {
                    case "count" -> Comparator.comparingInt(News::getNewsCheckCount).reversed();
                    case "like" -> Comparator.comparingInt(News::getNewsLikeCount).reversed();
                    default -> Comparator.comparing(News::getNewsDate).reversed();
                })
                .toList();
    }

    public List<News> getRecentNewsAboutStock(){
        return newsRepository.findByNewsTopicOrderByNewsDateDesc("코스피");
//        return newsRepository.findRecentNewsAboutStock();
    }

    public List<News> getRecentNewsAboutNasdaq() {
        return newsRepository.findByNewsTopicOrderByNewsDateDesc("나스닥");
//        return newsRepository.findRecentNewsAboutNasdaq();
    }

    public List<RecentNews> getRecentNewsList(User user){
        return recentNewsRepository.findByUserUserIdOrderByRecentNewsDateDesc(user.getUserId());
    }

    @Transactional
    public void addNewsComment(Long newsId, User user, String newsCommentContent) {
        News news = newsRepository.findById(newsId).orElse(null);
//        News news = newsRepository.findById(newsId);
        NewsComment newsComment = NewsComment.makeNewsCommentItem(news, user, newsCommentContent);
        newsCommentRepository.save(newsComment);
    }

    @Transactional
    public void deleteNewsComment(Long newsCommentId) {
        NewsComment newsComment = newsCommentRepository.findById(newsCommentId).orElse(null);
        newsCommentRepository.delete(newsComment);
    }

    public List<NewsComment> findCommentById(Long newsId) {
        return newsCommentRepository.findByNewsNewsId(newsId);
    }

    @Transactional
    public News processDetailPageNews(Long newsId, User user, String isLike) {
        News news = this.findById(newsId);
        if(user != null) this.processRecentNews(news, user);
        if(isLike.isEmpty()) this.checkCountAdd(news);
        return news;
    }

    @Transactional
    public void processRecommend(Long newsId,User user,String action) {
        News news = this.findById(newsId);
        if(action.equals("like")){
            this.addLikeCount(news, user);
            return;
        }
        this.subLikeCount(news, user);
    }


//    @Scheduled(fixedDelay = 600000000)
    @Scheduled(cron = "0 0 9,15 * * *")
    public void getKospiNewsData(){
        getNewsDataUseApi("코스피");
    }

//    @Scheduled(fixedDelay = 600000000)
    @Scheduled(cron = "0 0 6,22 * * *")
    public void getNasdaqNewsData(){
        getNewsDataUseApi("나스닥");
    }

    public List<News> getRecentNewsAboutTopic(String topic){
        return newsRepository.findRecentNewsAboutTopic(topic);
    }
    private List<News> getRecentNewsAboutTitle(String topic){
        return newsRepository.findRecentNewsAboutTopicTitle(topic);
    }

    private List<News> getRecentNewsAboutContent(String topic){
        return newsRepository.findRecentNewsAboutTopicContent(topic);
    }

    private News findById(Long newsId){
        return newsRepository.findById(newsId)
                .orElse(null);
//        return newsRepository.findById(newsId);
    }

    private void checkCountAdd(News news){
        news.checkCount();
    }

    private void addLikeCount(News news, User user){
        List<LikeNews> likeNews = likeNewsRepository.findByUserUserIdAndNewsNewsId(user.getUserId(), news.getNewsId());
        List<DislikeNews> dislikeNews = disLikeNewsRepository.findByUserUserIdAndNewsNewsId(user.getUserId(), news.getNewsId());

        if(likeNews.isEmpty() && dislikeNews.isEmpty()){
            news.addLike();
            likeNewsRepository.save(LikeNews.makeLikeNews(news, user));
            return;
        }else if(likeNews.isEmpty()){
            news.addLike();
            disLikeNewsRepository.delete(dislikeNews.get(0));
            return;
        }

        news.subLike();
        likeNewsRepository.delete(likeNews.get(0));
    }

    private void subLikeCount(News news, User user){
        List<LikeNews> likeNews = likeNewsRepository.findByUserUserIdAndNewsNewsId(user.getUserId(), news.getNewsId());
        List<DislikeNews> dislikeNews = disLikeNewsRepository.findByUserUserIdAndNewsNewsId(user.getUserId(), news.getNewsId());

        if(dislikeNews.isEmpty() && likeNews.isEmpty()){
            news.subLike();
            disLikeNewsRepository.save(DislikeNews.makeDislikeNews(news, user));
            return;
        }else if(dislikeNews.isEmpty()){
            news.subLike();
            likeNewsRepository.save(likeNews.get(0));
            return;
        }

        news.addLike();
        disLikeNewsRepository.delete(dislikeNews.get(0));
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
        try{
            WebClient webClient = getWebClient();
            String apiUrl = getApiUrl(topic);

            Map<String, Object> newsData = webClient.get()
                    .uri(apiUrl)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();

            List<Item> items = (List<Item>) newsData.get("items");
            List<News> compareTopic = newsRepository.findAll();
//            List<News> compareTopic = newsRepository.findAllNewsAboutTopic();

            for (int i = 0; i < items.size(); i++) {
                ObjectMapper mapper = new ObjectMapper();

                Item item = mapper.convertValue(items.get(i), Item.class);
                item.cleanHtmlCode();

                String newsLink = item.getLink() != null ? item.getLink() : item.getOriginallink();
                Optional<News> existingNews = compareTopic.stream()
                        .filter(v -> ((v.getNewsURL().equals(newsLink))))
                        .findAny();

                if(existingNews.isEmpty()){
                    saveNews(item, topic);
                }
            }
        } catch (WebClientResponseException.BadRequest ex){
            log.error("error = {}", ex.toString());
        }
    }

    private void addRecentNews(News news, User user) {
        RecentNews recentNews = RecentNews.makeRecentNews(news,user);
        recentNewsRepository.save(recentNews);
    }

    private void updateRecentNews(RecentNews recentNews){
        recentNews.setRecentNewsDate();
    }

    private RecentNews getRecentNewsAlreadySeen(News news, User user) {
        List<RecentNews> recentNews = recentNewsRepository.findByUserUserIdAndNewsNewsId(user.getUserId(), news.getNewsId());
        if(recentNews.isEmpty()){
            return null;
        }
        return recentNews.get(0);
    }

    private void processRecentNews(News news, User user) {
        RecentNews recentNews = this.getRecentNewsAlreadySeen(news, user);
        if(recentNews == null) {
            this.addRecentNews(news, user);
            return;
        }
        this.updateRecentNews(recentNews);
    }

    public void loadInterestWordNews(String interestWord) {
        for(String word : interestWord.split(",")){
            getNewsData(word.trim());
        }
    }
}
