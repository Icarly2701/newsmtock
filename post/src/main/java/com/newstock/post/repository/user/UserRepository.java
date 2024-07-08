package com.newstock.post.repository.user;

import com.newstock.post.domain.user.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long>, CustomUserRepository {
    List<User> findById(String userId);
}
