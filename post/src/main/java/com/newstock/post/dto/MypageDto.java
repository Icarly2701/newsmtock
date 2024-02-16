package com.newstock.post.dto;

import com.newstock.post.domain.news.News;
import com.newstock.post.domain.news.RecentNews;
import com.newstock.post.domain.post.Post;
import com.newstock.post.domain.post.RecentPost;
import com.newstock.post.domain.user.PreferenceTitle;
import lombok.Data;

import java.util.List;

@Data
public class MypageDto {
    private List<PreferenceTitle> preferenceTitle;
    private List<News> preferenceNews;
    private List<RecentNews> recentNews;
    private List<RecentPost> recentPost;
    private List<Post> myPost;

    public MypageDto(List<PreferenceTitle> preferenceTitles, List<News> preferenceNews, List<RecentNews> recentNews, List<RecentPost> recentPost, List<Post> myPost) {
        this.preferenceTitle = preferenceTitles;
        this.preferenceNews = preferenceNews;
        this.recentNews = recentNews;
        this.recentPost = recentPost;
        this.myPost = myPost;
    }
}
