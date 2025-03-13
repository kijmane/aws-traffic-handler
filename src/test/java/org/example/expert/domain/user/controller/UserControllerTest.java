package org.example.expert.domain.user.controller;

import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
public class UserControllerTest {

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testSearchUsersByNicknamePerformance() {
        long startTime = System.nanoTime();

        Page<User> users = userRepository.findByNickname("testNickname", PageRequest.of(0, 10));

        long endTime = System.nanoTime();

        assertTrue(users.getTotalElements() > 0);

        long duration = endTime - startTime;
        System.out.println("Search performance duration: " + duration + " nanoseconds");

        assertTrue(duration < 1000000000, "Search duration should be less than 1 second.");
    }
}