package com.newstock.post.repository.post;

import com.newstock.post.domain.Category;
import com.newstock.post.domain.news.News;
import com.newstock.post.domain.post.Post;
import com.newstock.post.domain.user.User;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
@RequiredArgsConstructor
@Slf4j
public class PostRepository {

    private final EntityManager em;

    public List<Post> findAboutTopic(String category){
        return em.createQuery("select p from Post p where p.category.categoryContent = :category", Post.class)
                .setParameter("category", category)
                .getResultList();
    }

    public Long save(Post post){
        em.persist(post);
        return post.getPostId();
    }

    public Post findById(Long postId) {
        return em.find( Post.class, postId);
    }

    public List<Post> findByUser(User user) {
        return em.createQuery("select p from Post p where p.user.userId = :userId", Post.class)
                .setParameter("userId", user.getUserId())
                .getResultList();
    }

    public List<Post> findByTopic(String topic){
        return em.createQuery("select p from Post p join p.postContent pc where p.postTitle LIKE :title or pc.postContentText LIKE :contentText order by p.postDate DESC", Post.class)
                .setParameter("title","%" + topic + "%")
                .setParameter("contentText", "%" + topic + "%")
                .getResultList();
    }

    public List<Post> findByTopicTitle(String topic){
        return em.createQuery("select p from Post p join p.postContent pc where p.postTitle LIKE :title order by p.postDate DESC", Post.class)
                .setParameter("title","%" + topic + "%")
                .getResultList();
    }

    public List<Post> findByTopicContent(String topic){
        return em.createQuery("select p from Post p join p.postContent pc where pc.postContentText LIKE :contentText order by p.postDate DESC", Post.class)
                .setParameter("contentText", "%" + topic + "%")
                .getResultList();
    }

    public List<Post> findAboutTopicCount(String stock) {
        return em.createQuery("select p from Post p where p.category.categoryContent = :topic order by p.postCheckCount DESC", Post.class)
                .setParameter("topic",stock)
                .getResultList();
    }

    public List<Post> findAboutTopicLike(String stock) {
        return em.createQuery("select p from Post p where p.category.categoryContent = :topic order by p.postLikeCount DESC", Post.class)
                .setParameter("topic",stock)
                .getResultList();
    }

    public List<Post> findAboutTopicDate(String stock) {
        return em.createQuery("select p from Post p where p.category.categoryContent = :topic order by p.postDate DESC", Post.class)
                .setParameter("topic",stock)
                .getResultList();
    }

    public void deletePost(Post post) {
        em.remove(post);
    }

    public void saveCategory(Category category) {
        em.persist(category);
    }

    public Category findCategory(String category){
        return (Category) em.createQuery("select c from Category c where c.categoryContent = :category")
                .setParameter("category", category)
                .getSingleResult();
    }
}
