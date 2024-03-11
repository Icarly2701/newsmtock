package com.newstock.post.controller;

import com.newstock.post.service.NewsService;
import com.newstock.post.domain.user.User;
import com.newstock.post.dto.auth.LoginDto;
import com.newstock.post.dto.auth.SignupDto;
import com.newstock.post.service.UserService;
import com.newstock.post.web.Login;
import jakarta.servlet.http.Cookie;
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

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SignupLoginController {

    private final UserService userService;
    private final NewsService newsService;

    @PostMapping("/signup")
    public String signup(@Validated @ModelAttribute("signupData") SignupDto signupDto,
                         BindingResult bindingResult){
        userService.validateLoginData(signupDto, bindingResult);
        if (bindingResult.hasErrors()) {
            return "signuppage";
        }
        userService.processSignup(signupDto);
        newsService.loadInterestWordNews(signupDto.getInterestWord());
        return "redirect:/login";
    }

    @GetMapping("/signup")
    public String viewSignupPage(Model model){
        model.addAttribute("signupData", new SignupDto());
        return "signuppage";
    }

    @GetMapping("/loginSuccess")
    public String login() throws UnsupportedEncodingException {
        return "redirect:/";
    }

    @GetMapping("/login")
    public String viewLoginPage(Model model){
        model.addAttribute("loginData", new LoginDto());
        return "loginpage";
    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if(authentication != null){
            new SecurityContextLogoutHandler().logout(request,response,authentication);
        }

        return "redirect:/";
    }
}
