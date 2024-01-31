package com.newstock.post.api;

import lombok.Getter;
import lombok.Setter;
import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;

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

    public void cleanHtmlCode() {
        if (description != null) {
            description = Jsoup.clean(description, Safelist.none());
        }
        if(title != null){
            title = Jsoup.clean(title, Safelist.none());
        }
    }
}
