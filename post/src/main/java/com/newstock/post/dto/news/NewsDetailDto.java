package com.newstock.post.dto.news;

import com.newstock.post.domain.news.News;
import com.newstock.post.domain.news.NewsComment;
import com.newstock.post.domain.user.User;
import lombok.Data;

import java.util.List;

@Data
public class NewsDetailDto {
    private News content;
    private List<NewsComment> newsCommentList;
    private User viewUser;

    public NewsDetailDto(News news, List<NewsComment> newsCommentList, User user) {
        this.content = news;
        this.newsCommentList = newsCommentList;
        this.viewUser = user;
    }
}
