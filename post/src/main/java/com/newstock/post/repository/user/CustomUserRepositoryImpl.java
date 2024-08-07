package com.newstock.post.repository.user;

import com.newstock.post.domain.user.PreferenceTitle;
import com.newstock.post.domain.user.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class CustomUserRepositoryImpl implements CustomUserRepository{

    private final EntityManager em;

    public Long save(User user){
        em.persist(user);
        return user.getUserId();
    }

    public User findById(Long userId) {
        return em.find(User.class, userId);
    }

    public List<User> findByUserId(String userId){
        return em.createQuery("select u from User u where u.id = :userId", User.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public List<PreferenceTitle> findUserPreferenceTitle(Long userId){
        return em.createQuery("select p from PreferenceTitle p where p.user.userId = :userId", PreferenceTitle.class)
                .setParameter("userId", userId)
                .getResultList();
    }

    public boolean existsById(String id) {
        Long count = em.createQuery("SELECT COUNT(u) FROM User u WHERE u.id = :id", Long.class)
                .setParameter("id", id)
                .getSingleResult();
        return count > 0;
    }
}
