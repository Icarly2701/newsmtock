package com.newstock.post.domain.user;

import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
@Table(name = "preference_title")
public class PreferenceTitle {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long preferenceTitleId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(length = 1000)
    private String preferenceTitle;

    public static PreferenceTitle preferenceTitle(User user, String preferenceTitle){
        PreferenceTitle preferenceItem = new PreferenceTitle();
        preferenceItem.user = user;
        preferenceItem.preferenceTitle = preferenceTitle;
        return preferenceItem;
    }
}
