package com.newstock.post.dto.news;

import com.newstock.post.domain.news.NewsComment;
import lombok.Data;

@Data
public class NewsCommentDto {
    private Long newsCommentId;
    private String newsCommentUserNickname;
    private String newsCommentContent;

    public NewsCommentDto(NewsComment newsComment){
        this.newsCommentId = newsComment.getNewsCommentId();
        this.newsCommentContent = newsComment.getNewsCommentContent();
        this.newsCommentUserNickname = newsComment.getUser().getNickname();
    }
}
