package com.newstock.post.repository.post;

import com.newstock.post.domain.Category;
import com.newstock.post.domain.post.Post;
import com.newstock.post.domain.user.User;
import jakarta.persistence.EntityManager;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@RequiredArgsConstructor
@Slf4j
public class CustomPostRepositoryImpl implements CustomPostRepository{

    private final EntityManager em;

    @Override
    public List<Post> findByTopic(String topic){
        return em.createQuery("select p from Post p join p.postContent pc where p.postTitle LIKE :title or pc.postContentText LIKE :contentText order by p.postDate DESC", Post.class)
                .setParameter("title","%" + topic + "%")
                .setParameter("contentText", "%" + topic + "%")
                .getResultList();
    }

    @Override
    public List<Post> findByTopicTitle(String topic){
        return em.createQuery("select p from Post p join p.postContent pc where p.postTitle LIKE :title order by p.postDate DESC", Post.class)
                .setParameter("title","%" + topic + "%")
                .getResultList();
    }

    @Override
    public List<Post> findByTopicContent(String topic){
        return em.createQuery("select p from Post p join p.postContent pc where pc.postContentText LIKE :contentText order by p.postDate DESC", Post.class)
                .setParameter("contentText", "%" + topic + "%")
                .getResultList();
    }

    @Override
    @Transactional
    public void deletePost(Post post) {
        em.remove(em.contains(post) ? post : em.merge(post));
    }

}
