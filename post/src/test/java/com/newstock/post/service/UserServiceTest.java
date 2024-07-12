package com.newstock.post.service;

import com.newstock.post.domain.user.PreferenceTitle;
import com.newstock.post.domain.user.User;
import com.newstock.post.repository.user.PreferenceTitleRepository;
import com.newstock.post.repository.user.UserRepository;
import jakarta.transaction.Transactional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;
@Profile("test")
@SpringBootTest
@ExtendWith(SpringExtension.class)
@Transactional
class UserServiceTest {

    @Autowired
    UserService userService;

    @Autowired
    PreferenceTitleRepository preferenceTitleRepository;

    @Test
    public void 유저_생성_테스트() throws Exception{
        //given
        User test1 = User.makeTestUser("test5", "asdfasdf");

        //when
        Long user1 = userService.save(test1);
        User user = userService.findByUserId("test5");

        //then
        Assertions.assertThat(user1).isEqualTo(user.getUserId());
    }

    @Test
    public void 유저_선호_주제_추가_테스트() throws Exception{
        //given
        User test1 = User.makeTestUser("test5", "asdfasdf");
        Long testId = userService.save(test1);

        //when
        userService.addPreferenceTitle("테스트", testId);
        userService.addPreferenceTitle("주제", testId);

        List<PreferenceTitle> testTitles = preferenceTitleRepository.findByUserUserId(testId);
        List<String> preferenceTitles = testTitles.stream()
                .map(PreferenceTitle::getPreferenceTitle)
                .toList();
        //then
        Assertions.assertThat(preferenceTitles.size()).isEqualTo(2);
        Assertions.assertThat(preferenceTitles.contains("주제")).isTrue();
        Assertions.assertThat(preferenceTitles.contains("테스트")).isTrue();
    }

    @Test
    public void 유저_선호_주제_삭제_테스트() throws Exception{
        //given
        User test1 = User.makeTestUser("test5", "asdfasdf");
        Long testId = userService.save(test1);
        userService.addPreferenceTitle("테스트", testId);
        userService.addPreferenceTitle("주제", testId);
        List<PreferenceTitle> tempTitles = preferenceTitleRepository.findByUserUserId(testId);

        //when
        Long preferenceTitleId = tempTitles.get(0).getPreferenceTitleId();
        userService.deletePreferenceTitle(preferenceTitleId);
        List<PreferenceTitle> testTitles = preferenceTitleRepository.findByUserUserId(testId);

        //then
        Assertions.assertThat(testTitles.size()).isEqualTo(1);
    }

    @Test
    public void 회원가입_주제_저장_테스트1() throws Exception{
        //given
        userService.save(User.makeTestUser("test5", "asdfasdf"));
        User user = userService.findByUserId("test5");

        String preferenceWord = "       ";
        String preferenceWord2 = "   ,    ";

        //when
        userService.savePreferenceTitle(user, preferenceWord);
        userService.savePreferenceTitle(user, preferenceWord2);

        List<PreferenceTitle> preferenceTitles = preferenceTitleRepository.findByUserUserId(user.getUserId());

        //then
        Assertions.assertThat(preferenceTitles.isEmpty()).isTrue();
    }

    @Test
    public void 회원가입_주제_저장_테스트2() throws Exception{
        //given
        userService.save(User.makeTestUser("test5", "asdfasdf"));
        User user = userService.findByUserId("test5");

        String preferenceWord = "     ,지지자다다 , ";
        String preferenceWord2 = " , 무라차차차 ,  마라디자 , ";

        //when
        userService.savePreferenceTitle(user, preferenceWord);
        userService.savePreferenceTitle(user, preferenceWord2);

        List<PreferenceTitle> preferenceTitles = preferenceTitleRepository.findByUserUserId(user.getUserId());
        List<String> nameList = preferenceTitles.stream().map(PreferenceTitle::getPreferenceTitle).toList();

        //then
        Assertions.assertThat(preferenceTitles.size()).isEqualTo(3);
        Assertions.assertThat(nameList.contains("지지자다다")).isTrue();
        Assertions.assertThat(nameList.contains("무라차차차")).isTrue();
        Assertions.assertThat(nameList.contains("마라디자")).isTrue();
    }
}