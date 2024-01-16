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
    @JoinColumn(name = "user_id")
    private User user;

    private String preferenceTitle;
}
