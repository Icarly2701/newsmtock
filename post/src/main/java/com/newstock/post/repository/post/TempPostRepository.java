package com.newstock.post.repository.post;

import com.newstock.post.domain.post.TempPost;
import com.newstock.post.domain.user.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@Slf4j
@RequiredArgsConstructor
public class TempPostRepository {

    private final EntityManager em;

    public void save(TempPost tempPost){
        em.persist(tempPost);
    }

    public TempPost findByUser(User user){
        List <TempPost> postList = em.createQuery("select tp from TempPost tp where tp.user.userId = :userId", TempPost.class)
                .setParameter("userId", user.getUserId())
                .getResultList();

        if(postList.isEmpty()) return null;
        return postList.get(0);
    }

    public void deletePost(TempPost tempPost){
        em.remove(tempPost);
        em.flush();
    }
}
