package com.newstock.post.repository.user;

import com.newstock.post.domain.user.PreferenceTitle;
import com.newstock.post.domain.user.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PreferenceTitleRepository {

    private final EntityManager em;

    public void save(PreferenceTitle preferenceTitle){
        em.persist(preferenceTitle);
    }

    public void delete(PreferenceTitle preferenceTitle){
        em.remove(preferenceTitle);
    }

    public PreferenceTitle findById(Long preferenceTitleId){
        return em.find(PreferenceTitle.class, preferenceTitleId);
    }

    public List<PreferenceTitle> findByUserId(Long userId){
        return em.createQuery("select p from PreferenceTitle p where p.user.id = :userId", PreferenceTitle.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
