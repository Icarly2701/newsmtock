package com.newstock.post.api;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class Item {

    private String title;
    private String originallink;
    private String link;
    private String description;
    private String pubDate;
    private LocalDateTime localDateTime;
}
