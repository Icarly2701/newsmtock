package com.newstock.post.controller.rest_controller;

import com.newstock.post.domain.post.Post;
import com.newstock.post.domain.post.TempPost;
import com.newstock.post.domain.user.User;
import com.newstock.post.dto.post.*;
import com.newstock.post.service.PostService;
import com.newstock.post.web.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequiredArgsConstructor
@Slf4j
public class RestPostController {

    private final PostService postService;

    @GetMapping("/test/post/add/{category}")
    public PostAddDto viewAddPost(@Login User user,
                                  @PathVariable("category") String category,
                                  @ModelAttribute("tempData") TempPost tempData){
        return new PostAddDto(category,tempData,new PostUpload());
    }

    @PostMapping("/test/post/add/{category}")
    public String addPost(@Login User user,
                          @PathVariable("category") String category,
                          @Validated @ModelAttribute("postItem") PostUpload postUpload,
                          BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "postaddpage";
        }
        postService.processAddPost(user,category, postUpload);
        return "ok";
    }

    @GetMapping("/test/post/stock")
    public PostBoardDto viewPostStockBoard(@RequestParam(value = "target", required = false) String target,
                                     Model model){
        return new PostBoardDto(postService.findAboutTopic("stock")
                .stream()
                .sorted(target == null ? Comparator.comparing(Post::getPostDate).reversed() : switch(target){
                    case "count" -> Comparator.comparingInt(Post::getPostCheckCount).reversed();
                    case "like" -> Comparator.comparingInt(Post::getPostLikeCount).reversed();
                    default -> Comparator.comparing(Post::getPostDate).reversed();})
                .toList()
                .stream().map(PostTitleDto::new).collect(Collectors.toList()),
                "주식",
                "stock");
    }

    @GetMapping("/test/post/freeBoard")
    public PostBoardDto viewPostFreeBoard(@RequestParam(value = "target", required = false) String target){

        return new PostBoardDto(postService.findAboutTopic("freeBoard")
                .stream()
                .sorted(target == null ? Comparator.comparing(Post::getPostDate).reversed() : switch(target){
                    case "count" -> Comparator.comparingInt(Post::getPostCheckCount).reversed();
                    case "like" -> Comparator.comparingInt(Post::getPostLikeCount).reversed();
                    default -> Comparator.comparing(Post::getPostDate).reversed();})
                .toList()
                .stream().map(PostTitleDto::new).collect(Collectors.toList()),
                "자유",
                "freeBoard");
    }

    @GetMapping("/test/post/{postId}")
    public PostDetailDto viewPostDetail(@Login User user,
                                 @PathVariable("postId") Long postId,
                                 @ModelAttribute("isLike") String isLike){
        return PostDetailDto.makePostDetailDto(
                postService.processDetailPagePost(postId, user, isLike),
                user,
                postService.findCommentByPost(postId),
                postService.findImageByPost(postId));
    }

    @PostMapping("/test/post/{postId}")
    public String postLike(@Login User user,
                           @RequestParam String action,
                           @PathVariable("postId") Long postId,
                           RedirectAttributes redirectAttributes){
        postService.processRecommend(postId, user, action);
        redirectAttributes.addFlashAttribute("isLike", "notCount");
        return "ok";
    }

    @PostMapping("/test/post/{postId}/add")
    public String postAddComment(@Login User user,
                                 @PathVariable("postId") Long postId,
                                 @RequestParam("postCommentContent") String postCommentContent,
                                 RedirectAttributes redirectAttributes){
        postService.addPostComment(postId, user, postCommentContent);
        redirectAttributes.addFlashAttribute("isLike", "notCount");
        return "ok";
    }

    @PostMapping("/test/post/{postId}/delete/comment/{commentId}")
    public String postDeleteComment(@PathVariable("postId") Long postId,
                                    @PathVariable("commentId") Long postCommentId,
                                    RedirectAttributes redirectAttributes){
        postService.deletePostComment(postCommentId);
        redirectAttributes.addFlashAttribute("isLike", "notCount");
        return "ok";
    }

    @GetMapping("/test/post/delete/{postId}")
    public String deletePost(@PathVariable("postId") Long postId){
        postService.deletePost(postId);
        return "ok";
    }

    @GetMapping("/test/post/update/{postId}")
    public PostUpdateDto viewUpdatePost(@PathVariable("postId") Long postId,
                                 Model model){
        return new PostUpdateDto(
                postService.findById(postId),
                postService.findImageByPost(postId));
    }

    @PostMapping("/test/post/update/{postId}")
    public String updatePost(@Login User user,
                             @PathVariable("postId") Long postId,
                             @Validated @ModelAttribute("postItem") PostUploadUpdate postUpload,
                             BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "redirect:/post/update/" + postId;
        }
        postService.processUpdatePost(postId, postUpload);
        return "ok";
    }

    @PostMapping("/test/post/add/{category}/temp")
    public String saveTempPost(@Login User user,
                               @PathVariable("category") String category,
                               @ModelAttribute("postItem") PostUploadUpdate postUpload){
        postService.processTempPost(user, category,postUpload);
        return "ok";
    }

    @GetMapping("/test/post/add/temp/{category}")
    public String viewTempPost(@Login User user,
                               @PathVariable("category") String category,
                               RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("tempData", postService.findByUser(user));
        return "ok";
    }
}
