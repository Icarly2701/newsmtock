package com.newstock.post.service;

import com.newstock.post.domain.user.User;
import com.newstock.post.repository.user.CustomUserRepositoryImpl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class CustomUserDetailService implements UserDetailsService {

    private final CustomUserRepositoryImpl customUserRepositoryImpl;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        List<User> users = customUserRepositoryImpl.findByUserId(username);

        if(!users.isEmpty()){
            return new CustomUserDetails(users.get(0));
        }

        return null;
    }
}
