package org.example.expert.domain.user.service;

import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Optional;

@SpringBootTest
public class UserDataLoaderTest {

    @Autowired
    private UserRepository userRepository;

    private String testNickname;

    @BeforeEach
    void setUp() {
        Optional<User> user = userRepository.findAll().stream().findFirst();
        testNickname = user.map(User::getNickname).orElse("User123");
    }

    @Test
    void testSearchUsersByNicknamePerformance() {
        Pageable pageable = PageRequest.of(0, 10);

        long start = System.currentTimeMillis();
        userRepository.findByNickname(testNickname, pageable);
        long end = System.currentTimeMillis();

        System.out.println("닉네임 검색 소요 시간(ms): " + (end - start));
    }
}