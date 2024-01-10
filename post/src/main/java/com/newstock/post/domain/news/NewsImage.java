package com.newstock.post.domain.news;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class NewsImage {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long newsImageId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "news_content_id")
    private NewsContent newsContent;

    private String newsImagePath;
}
