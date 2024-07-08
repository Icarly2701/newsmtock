package com.newstock.post.repository.user;

import com.newstock.post.domain.user.PreferenceTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PreferenceTitleRepository extends JpaRepository<PreferenceTitle, Long> {
    List<PreferenceTitle> findByUserUserId(Long userId);
}
