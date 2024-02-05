package com.newstock.post.repository;

import com.newstock.post.domain.news.News;
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
public class UserRepository {

    private final EntityManager em;

    public Long save(User user){
        em.persist(user);
        return user.getUserId();
    }

    public void savePreferenceTitle(PreferenceTitle preferenceTitle){
        em.persist(preferenceTitle);
    }

    public User findById(Long userId) {
        return em.find(User.class, userId);
    }

    public List<User> findByUserId(String userId){
        return em.createQuery("select u from User u where u.id = :userId", User.class)
                .setParameter("userId", userId)
                .getResultList();
    }
}
