package com.newstock.post.controller.rest_controller;

import com.newstock.post.dto.auth.LoginDto;
import com.newstock.post.dto.auth.SignupDto;
import com.newstock.post.service.NewsService;
import com.newstock.post.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.UnsupportedEncodingException;

@RestController
@RequiredArgsConstructor
@Slf4j
public class SignupLoginController {

    private final UserService userService;
    private final NewsService newsService;

    @PostMapping("/test/signup")
    public String signup(@Validated @ModelAttribute("signupData") SignupDto signupDto,
                         BindingResult bindingResult){
        userService.validateLoginData(signupDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return "signuppage";
        }
        userService.processSignup(signupDto);
        newsService.loadInterestWordNews(signupDto.getInterestWord());
        return "ok";
    }

    @GetMapping("/test/signup")
    public SignupDto viewSignupPage(){
        return new SignupDto();
    }

    @GetMapping("/test/loginSuccess")
    public String login() throws UnsupportedEncodingException {
        return "ok";
    }

    @GetMapping("/test/login")
    public LoginDto viewLoginPage(Model model){
        return new LoginDto();
    }

    @GetMapping("/test/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null){
            new SecurityContextLogoutHandler().logout(request,response,authentication);
        }

        return "ok";
    }
}
