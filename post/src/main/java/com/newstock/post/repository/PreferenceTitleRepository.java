package com.newstock.post.repository;

import com.newstock.post.domain.user.PreferenceTitle;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PreferenceTitleRepository {

    private final EntityManager em;

    public void save(PreferenceTitle preferenceTitle){
        em.persist(preferenceTitle);
    }

    public void delete(PreferenceTitle preferenceTitle){
        log.info("preferenceTitle = {}", preferenceTitle.getPreferenceTitle());
        em.remove(preferenceTitle);
    }

    public PreferenceTitle findById(Long preferenceTitleId){
        return em.find(PreferenceTitle.class, preferenceTitleId);
    }
}
