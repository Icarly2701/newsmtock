package com.newstock.post.service;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.newstock.post.api.Item;
import com.newstock.post.domain.Target;
import com.newstock.post.domain.news.*;
import com.newstock.post.domain.user.PreferenceTitle;
import com.newstock.post.domain.user.User;
import com.newstock.post.dto.news.NewsDto;
import com.newstock.post.repository.news.*;
import com.newstock.post.repository.news.newsrep.NewsRepository;
import com.newstock.post.repository.user.PreferenceTitleRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final NewsContentRepository newsContentRepository;

    public List<News> getNewsData(String topic){
        return getNewsDataUseApi(topic);
    }

    public void getNewsData(Long userId){
        preferenceTitleRepository.findByUserUserId(userId)
                .forEach(v -> getNewsDataUseApi(v.getPreferenceTitle()));
    }

    public News saveNews(Item item, String topic){
        try{
            News news = News.makeNewsItem(item, topic);
            log.info("제목: {}", news.getNewsHeadline());
            newsRepository.upsertNews(news.getNewsURL(),
                    news.getNewsHeadline(),
                    news.getNewsDate(),
                    news.getNewsCheckCount(),
                    news.getNewsLikeCount(),
                    news.getNewsTopic());
            News insertNews = newsRepository.findByNewsURL(news.getNewsURL());
            log.info("id값: {}", insertNews.getNewsId());

            if(insertNews.getNewsContent() == null){
                log.info("null일 때 값 확인: {}", insertNews.getNewsHeadline());
                NewsContent newsContent = NewsContent.makeNewsContent(item.getDescription(), insertNews);
                newsContentRepository.save(newsContent);
                insertNews.setNewsContent(newsContent);
            } else{
                log.info("null이 아닐 때 값 확인: {}", insertNews.getNewsHeadline());
            }
            return news;
        }catch(DataIntegrityViolationException e){
            log.info("중복 삽입 에러 처리");
        }
        return null;
    }

    public List<News> getPopularNews(){
        Pageable pageable = PageRequest.of(0, 15);
        return newsRepository.findAllByOrderByNewsCheckCount(pageable);
//        return newsRepository.findPopularNews();
    }

    public List<News> getUserPreferenceNews(List<PreferenceTitle> userPreferenceTitle) {
        return userPreferenceTitle.stream()
                .map(preferenceTitle -> this.getRecentNewsAboutTopic(preferenceTitle.getPreferenceTitle()))
                .flatMap(List::stream)
                .collect(Collectors.toList());
    }

    public List<News> getSearchData(String keyword, Target target) {
        return getNewsData(keyword);
//        return target == null ? this.getRecentNewsAboutTopic(keyword) : switch (target) {
//            case title_content -> this.getRecentNewsAboutTopic(keyword);
//            case content -> this.getRecentNewsAboutTitle(keyword);
//            default -> this.getRecentNewsAboutContent(keyword);
//        };
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
        Pageable pageable = PageRequest.of(0,15);
        return newsRepository.findByNewsTopicOrderByNewsDateDesc("코스피", pageable);
    }

    public List<News> getRecentNewsAboutNasdaq() {
        Pageable pageable = PageRequest.of(0,15);
        return newsRepository.findByNewsTopicOrderByNewsDateDesc("나스닥", pageable);
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


//    @Scheduled(cron = "0 0 9,15 * * *")

    @Transactional(noRollbackFor = DataIntegrityViolationException.class)
    @Scheduled(fixedDelay = 7200000)
    public void getNewsData(){
        log.info("검색 시작 시간: {}", System.currentTimeMillis());
        String [] stockKeyword = {"주식", "증권", "금융", "코스피", "코스닥", "나스닥", "NASDAQ", "다우존스", "금리", "증시", "부동산"};
        for(String keyword : stockKeyword){
            List<News> newsDataUseApi = getNewsDataUseApi(keyword);
        }
        log.info("검색 끝 시간: {}", System.currentTimeMillis());
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
                .queryParam("display", 20)
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


    private List<News> getNewsDataUseApi(String topic) {
        List<News> result = new ArrayList<>();
        try{
            WebClient webClient = getWebClient();
            String apiUrl = getApiUrl(topic);
            Map<String, Object> newsData = webClient.get()
                    .uri(apiUrl)
                    .retrieve()
                    .bodyToMono(new ParameterizedTypeReference<Map<String, Object>>() {})
                    .block();
            List<Item> items = (List<Item>) newsData.get("items");

            for (int i = 0; i<items.size(); i++) {
                ObjectMapper mapper = new ObjectMapper();
                Item item = mapper.convertValue(items.get(i), Item.class);
                item.cleanHtmlCode();
                News news = saveNews(item, topic);
                if(news != null){
                    result.add(news);
                }else{
                    log.info("값이 제대로 null로 반환되는지 확인");
                }

            }
            return result;
        } catch (WebClientResponseException.BadRequest ex){
            log.error("error = {}", ex.toString());
        }
        return result;
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
