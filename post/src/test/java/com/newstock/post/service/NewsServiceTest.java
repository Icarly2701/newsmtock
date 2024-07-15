package com.newstock.post.service;

import com.newstock.post.repository.news.newsrep.CustomNewsRepositoryImpl;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

@Transactional
@SpringBootTest
@ExtendWith(SpringExtension.class)
class NewsServiceTest {

    @Autowired
    NewsService newsService;


}