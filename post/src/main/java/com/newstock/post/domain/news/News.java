package com.newstock.post.domain.news;

import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class News {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long newsId;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "news")
    private NewsContent newsContent;

    private String newsHeadline;
    private String newsURL;
    private LocalDateTime newsDate;
    private int newsCheckCount;

}
