package com.newstock.post.controller;

import com.newstock.post.domain.post.*;
import com.newstock.post.domain.user.User;
import com.newstock.post.dto.post.*;
import com.newstock.post.service.PostService;
import com.newstock.post.web.Login;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Comparator;

@Controller
@RequiredArgsConstructor
@Slf4j
public class PostController {

    private final PostService postService;

    @GetMapping("/post/add/{category}")
    public String viewAddPost(@Login User user, @PathVariable("category") String category, @ModelAttribute("tempData") TempPost tempData ,Model model){
        model.addAttribute("postAddDto", new PostAddDto(category,tempData,new PostUpload()));
        return "postaddpage";
    }

    @PostMapping("/post/add/{category}")
    public String addPost(@Login User user,
                          @PathVariable("category") String category,
                          @Validated @ModelAttribute("postItem") PostUpload postUpload,
                          BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "postaddpage";
        }
        return "redirect:/post/" + postService.processAddPost(user,category, postUpload);
    }

    @GetMapping("/post/stock")
    public String viewPostStockBoard(@RequestParam(value = "target", required = false) String target,
                                     Model model){

        model.addAttribute("postBoardData", new PostBoardDto(postService.findAboutTopic("stock")
                .stream()
                .sorted(target == null ? Comparator.comparing(Post::getPostDate).reversed() : switch(target){
                    case "count" -> Comparator.comparingInt(Post::getPostCheckCount).reversed();
                    case "like" -> Comparator.comparingInt(Post::getPostLikeCount).reversed();
                    default -> Comparator.comparing(Post::getPostDate).reversed();})
                .toList(),
                "주식",
                "stock"));

        return "postpage";
    }

    @GetMapping("/post/freeBoard")
    public String viewPostFreeBoard(@RequestParam(value = "target", required = false) String target,
                                    Model model){

        model.addAttribute("postBoardData", new PostBoardDto(postService.findAboutTopic("freeBoard")
                .stream()
                .sorted(target == null ? Comparator.comparing(Post::getPostDate).reversed() : switch(target){
                    case "count" -> Comparator.comparingInt(Post::getPostCheckCount).reversed();
                    case "like" -> Comparator.comparingInt(Post::getPostLikeCount).reversed();
                    default -> Comparator.comparing(Post::getPostDate).reversed();})
                .toList(),
                "자유",
                "freeBoard"));

        return "postpage";
    }

    @GetMapping("/post/{postId}")
    public String viewPostDetail(@Login User user,
                                 @PathVariable("postId") Long postId,
                                 @ModelAttribute("isLike") String isLike,
                                 Model model){
        model.addAttribute("postDetailData",PostDetailDto.makePostDetailDto(
                postService.processDetailPagePost(postId, user, isLike),
                user,
                postService.findCommentByPost(postId),
                postService.findImageByPost(postId)));
        return "postdetailpage";
    }

    @PostMapping("/post/{postId}")
    public String postLike(@Login User user,
                           @RequestParam String action,
                           @PathVariable("postId") Long postId,
                           RedirectAttributes redirectAttributes){
        postService.processRecommend(postId, user, action);
        redirectAttributes.addFlashAttribute("isLike", "notCount");
        return "redirect:/post/" + postId;
    }

    @PostMapping("/post/{postId}/add")
    public String postAddComment(@Login User user,
                                 @PathVariable("postId") Long postId,
                                 @RequestParam("postCommentContent") String postCommentContent,
                                 RedirectAttributes redirectAttributes){
        postService.addPostComment(postId, user, postCommentContent);
        redirectAttributes.addFlashAttribute("isLike", "notCount");
        return "redirect:/post/" + postId;
    }

    @PostMapping("/post/{postId}/delete")
    public String postDeleteComment(@PathVariable("postId") Long postId,
                                 @RequestParam("postCommentId") Long postCommentId,
                                 RedirectAttributes redirectAttributes){
        postService.deletePostComment(postCommentId);
        redirectAttributes.addFlashAttribute("isLike", "notCount");
        return "redirect:/post/" + postId;
    }

    @GetMapping("/post/delete/{postId}")
    public String deletePost(@PathVariable("postId") Long postId){
        postService.deletePost(postId);
        return "redirect:/mypage";
    }

    @GetMapping("/post/update/{postId}")
    public String viewUpdatePost(@PathVariable("postId") Long postId,
                                 Model model){
        model.addAttribute("postUpdateData", new PostUpdateDto(
                postService.findById(postId),
                postService.findImageByPost(postId)));
        return "postupdatepage";
    }

    @PostMapping("/post/update/{postId}")
    public String updatePost(@Login User user,
                             @PathVariable("postId") Long postId,
                             @Validated @ModelAttribute("postItem") PostUploadUpdate postUpload,
                             BindingResult bindingResult){
        if (bindingResult.hasErrors()) {
            return "redirect:/post/update/" + postId;
        }
        postService.processUpdatePost(postId, postUpload);
        return "redirect:/post/" + postId;
    }

    @PostMapping("/post/add/{category}/temp")
    public String saveTempPost(@Login User user,
                               @PathVariable("category") String category,
                               @ModelAttribute("postItem") PostUploadUpdate postUpload){
        postService.processTempPost(user, category,postUpload);
        return "redirect:/post/add/" + category;
    }

    @GetMapping("/post/add/temp/{category}")
    public String viewTempPost(@Login User user,
                               @PathVariable("category") String category,
                               RedirectAttributes redirectAttributes){
        redirectAttributes.addFlashAttribute("tempData", postService.findByUser(user));
        return "redirect:/post/add/" + category;
    }
}
