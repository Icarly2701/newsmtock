package com.newstock.post.controller;

import com.newstock.post.dto.SignupDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@Slf4j
public class SignupLoginController {

    @PostMapping("/signup")
    public String signup(@ModelAttribute("signupData") SignupDto signupDto){
        return "redirect:/";
    }

    @GetMapping("/signup")
    public String viewSignupPage(){
        return "signuppage";
    }

    @PostMapping("/")
    public String login(@RequestParam("loginId") String loginId,
                        @RequestParam("loginPassword") String loginPassword){
        return "redirect:/";
    }

    @GetMapping("/login")
    public String viewLoginPage(){
        return "loginpage";
    }
}
