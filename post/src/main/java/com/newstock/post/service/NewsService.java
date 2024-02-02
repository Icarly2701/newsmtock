package com.newstock.post.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newstock.post.api.Item;
import com.newstock.post.domain.news.News;
import com.newstock.post.repository.NewsRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpHeaders;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class NewsService {

    private final NewsRepository newsRepository;

    public Long save(News news){
        newsRepository.save(news);
        return news.getNewsId();
    }

    public List<News> getRecentNewsAboutStock(){
        return newsRepository.findRecentNewsAboutStock();
    };

    public List<News> getRecentNewsAboutTopic(String topic){
        return newsRepository.findRecentNewsAboutTopic(topic);
    }

    public News findById(Long newsId){
        return newsRepository.findById(newsId);
    }

    @Transactional
    public void checkCountAdd(News news){
        news.checkCount();
    }

    @Transactional
    public void addLikeCount(News news){
        news.addLike();
    }

    @Transactional
    public void subLikeCount(News news){
        news.subLike();
    }

    public void getNewsData(String topic){
        getNewsDataUseApi(topic);
    }

    @Scheduled(fixedDelay = 300000000)
    public void getStockNewsData(){
        getNewsDataUseApi("코스피");
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
}
