package com.newstock.post.domain;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class Category {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long categoryId;

    @Column(nullable = false, length = 50)
    private String categoryContent;

    public static Category makeCategory(String categoryContent){
        Category category = new Category();
        category.categoryContent = categoryContent;
        return category;
    }
}
