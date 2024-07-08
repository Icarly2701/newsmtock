package com.newstock.post.repository.user;

import com.newstock.post.domain.user.PreferenceTitle;

import java.util.List;

public interface CustomUserRepository {
    List<PreferenceTitle> findUserPreferenceTitle(Long userId);
    boolean existsById(String id);
}
