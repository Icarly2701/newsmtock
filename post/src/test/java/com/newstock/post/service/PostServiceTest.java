package com.newstock.post.service;

import com.newstock.post.domain.Category;
import com.newstock.post.domain.post.Post;
import com.newstock.post.domain.post.PostComment;
import com.newstock.post.domain.post.TempPost;
import com.newstock.post.domain.user.User;
import com.newstock.post.dto.post.PostUpload;
import com.newstock.post.dto.post.PostUploadUpdate;
import com.newstock.post.repository.post.CategoryRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.Before;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@Profile("test")
@Transactional
@SpringBootTest
@ExtendWith(SpringExtension.class)
class PostServiceTest {

    @Autowired
    PostService postService;
    @Autowired
    CategoryRepository categoryRepository;
    @Autowired
    UserService userService;
    private User user;
    private User otherUser;
    @BeforeEach
    public void setUp(){
        user = User.makeTestUser("testUsing", "testNickname");
        otherUser = User.makeTestUser("otherUser", "different");
        userService.save(user);
        userService.save(otherUser);
        categoryRepository.save(Category.makeCategory("test"));
    }

    @Test
    public void 포스트_저장_및_조회_테스트() throws Exception{
        //given
        PostUpload postUpload = makeTempPost("test1", "testC1");
        PostUpload postUpload2 = makeTempPost("test2", "testC2");
        PostUpload postUpload3 = makeTempPost("test3", "testC3");

        //when
        postService.processAddPost(user, "test", postUpload);
        postService.processAddPost(user, "test", postUpload2);
        postService.processAddPost(user, "test", postUpload3);
        //then
        List<Post> postList = postService.getMyPostList(user);
        Assertions.assertThat(postList.size()).isEqualTo(3);

        List<String> nameList = postList.stream().map(Post::getPostTitle).toList();
        Assertions.assertThat(nameList.contains("test1")).isTrue();
        Assertions.assertThat(nameList.contains("test2")).isTrue();
        Assertions.assertThat(nameList.contains("test3")).isTrue();

    }
    
    @Test
    public void 포스트_수정_테스트() throws Exception{
        //given
        PostUpload postUpload = makeTempPost("test", "testC");
        Long postId = postService.processAddPost(user, "test", postUpload);

        //when
        Post beforeUpdate = postService.findById(postId);
        postService.processUpdatePost(postId, makeUpdate("update", "updateContent"));
        Post afterUpdate = postService.findById(postId);

        //then
        Assertions.assertThat(beforeUpdate.getPostId()).isEqualTo(afterUpdate.getPostId());
        Assertions.assertThat(afterUpdate).isEqualTo(beforeUpdate);
        Assertions.assertThat(afterUpdate.getPostTitle()).isEqualTo("update");
    }
    
    @Test
    public void 포스트_삭제_테스트() throws Exception{
        //given
        PostUpload postUpload = makeTempPost("test", "testC");
        Long postId = postService.processAddPost(user, "test", postUpload);

        //when
        postService.deletePost(postId);

        //then
        List<Post> postList = postService.getMyPostList(user);
        Assertions.assertThat(postList.isEmpty()).isTrue();
    }
    
    @Test
    public void 포스트_댓글_추가_테스트() throws Exception{
        //given
        PostUpload postUpload = makeTempPost("test", "testC");
        Long postId = postService.processAddPost(user, "test", postUpload);

        //when
        postService.addPostComment(postId, user, "comment1");
        postService.addPostComment(postId, otherUser, "comment2");

        //then
        List<PostComment> postCommentList = postService.getPostCommentList(postId);
        List<String> idList = postCommentList.stream()
                .map(PostComment::getUser)
                .map(User::getId).toList();

        Assertions.assertThat(postCommentList.size()).isEqualTo(2);
        Assertions.assertThat(idList.contains("testUsing")).isTrue();
        Assertions.assertThat(idList.contains("otherUser")).isTrue();
    }

    @Test
    public void 포스트_댓글_삭제_테스트() throws Exception{
        //given
        PostUpload postUpload = makeTempPost("test", "testC");
        Long postId = postService.processAddPost(user, "test", postUpload);

        postService.addPostComment(postId, user, "comment1");
        postService.addPostComment(postId, otherUser, "comment2");

        PostComment postComment = postService.getPostCommentList(postId).stream()
                .filter(comment -> comment.getUser().getId().equals("testUsing"))
                .findFirst().orElse(null);

        //when
        postService.deletePostComment(postComment.getPostCommentId());

        //then
        List<PostComment> postCommentList = postService.getPostCommentList(postId);
        List<String> idList = postCommentList.stream()
                .map(PostComment::getUser)
                .map(User::getId).toList();

        Assertions.assertThat(postCommentList.size()).isEqualTo(1);
        Assertions.assertThat(!idList.contains("testUsing")).isTrue();
        Assertions.assertThat(idList.contains("otherUser")).isTrue();
    }

    @Test
    public void 포스트_추천_테스트1() throws Exception{
        //given
        PostUpload postUpload = makeTempPost("test", "testC");
        Long postId = postService.processAddPost(user, "test", postUpload);

        //when
        postService.processRecommend(postId, user, "like");
        postService.processRecommend(postId, otherUser, "like");

        //then
        Post post = postService.findById(postId);
        Assertions.assertThat(post.getPostLikeCount()).isEqualTo(2);
    }

    @Test
    public void 포스트_추천_테스트2() throws Exception{
        //given
        PostUpload postUpload = makeTempPost("test", "testC");
        Long postId = postService.processAddPost(user, "test", postUpload);

        //when
        postService.processRecommend(postId, user, "like");
        postService.processRecommend(postId, user, "like");

        //then
        Post post = postService.findById(postId);
        Assertions.assertThat(post.getPostLikeCount()).isEqualTo(0);
    }

    @Test
    public void 포스트_추천_테스트3() throws Exception{
        //given
        PostUpload postUpload = makeTempPost("test", "testC");
        Long postId = postService.processAddPost(user, "test", postUpload);

        //when
        postService.processRecommend(postId, user, "like");
        postService.processRecommend(postId, user, "dislike");

        //then
        Post post = postService.findById(postId);
        Assertions.assertThat(post.getPostLikeCount()).isEqualTo(0);
    }

    @Test
    public void 포스트_추천_테스트4() throws Exception{
        //given
        PostUpload postUpload = makeTempPost("test", "testC");
        Long postId = postService.processAddPost(user, "test", postUpload);

        //when
        postService.processRecommend(postId, user, "like");
        postService.processRecommend(postId, otherUser, "dislike");

        //then
        Post post = postService.findById(postId);
        Assertions.assertThat(post.getPostLikeCount()).isEqualTo(0);
    }

    @Test
    public void 포스트_추천_테스트5() throws Exception{
        //given
        PostUpload postUpload = makeTempPost("test", "testC");
        Long postId = postService.processAddPost(user, "test", postUpload);

        //when
        postService.processRecommend(postId, user, "dislike");
        postService.processRecommend(postId, otherUser, "dislike");

        //then
        Post post = postService.findById(postId);
        Assertions.assertThat(post.getPostLikeCount()).isEqualTo(-2);
    }

    @Test
    public void 임시저장_테스트1() throws Exception{
        //given
        PostUploadUpdate postUploadUpdate = makeUpdate("testTitle", "testContent");

        //when
        postService.processTempPost(user, "test", postUploadUpdate);

        //then
        TempPost tempPost = postService.findByUser(user);
        Assertions.assertThat(tempPost).isNotNull();
    }

    @Test
    public void 임시저장_테스트3() throws Exception{
        //given
        //when
        //then
        TempPost tempPost = postService.findByUser(user);
        Assertions.assertThat(tempPost).isNull();
    }

    private static PostUpload makeTempPost(String title, String content) {
        PostUpload postUpload = new PostUpload();
        postUpload.setContent(content);
        postUpload.setTitle(title);
        postUpload.setFileList(new ArrayList<>());
        return postUpload;
    }

    private static PostUploadUpdate makeUpdate(String title, String content){
        PostUploadUpdate postUploadUpdate = new PostUploadUpdate();
        postUploadUpdate.setTitle(title);
        postUploadUpdate.setContent(content);
        postUploadUpdate.setFileList(new ArrayList<>());
        return postUploadUpdate;
    }
}