package com.newstock.post.web;

import com.newstock.post.domain.Gender;
import com.newstock.post.domain.user.User;
import com.newstock.post.dto.SignupDto;
import com.newstock.post.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PostInitializer implements ApplicationRunner {

    private final UserService userService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        makeDummyUser();

    }

    private void makeDummyUser() {
        SignupDto signupDto1 = makeDummy(25, "dldbtjd", Gender.MALE, "dbtjd0127", "이유성", "테슬라, 축구");
        SignupDto signupDto2 = makeDummy(23, "vkxkdi", Gender.FEMALE, "vkxkdi", "태국", "여행, 동남아");
        User user1 = User.makeuser(signupDto1);
        User user2 = User.makeuser(signupDto2);

        Long userId1 = userService.save(user1);
        Long userId2 = userService.save(user2);

        for(String interestWord: signupDto1.getInterestWord().split(",")){
            userService.savePreferenceTitle(userId1, interestWord.trim());
        }

        for(String interestWord: signupDto2.getInterestWord().split(",")){
            userService.savePreferenceTitle(userId2, interestWord.trim());
        }
    }

    private SignupDto makeDummy(int age, String id, Gender gender, String password, String nickname, String interestWord) {
        SignupDto signupDto = new SignupDto();
        signupDto.setAge(age);
        signupDto.setId(id);
        signupDto.setGender(gender);
        signupDto.setPassword(password);
        signupDto.setNickname(nickname);
        signupDto.setInterestWord(interestWord);
        return signupDto;
    }
}
