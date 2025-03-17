package org.example.expert.domain.user.service;

import lombok.RequiredArgsConstructor;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class UserDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    @Override
    public void run(String... args) throws Exception {
        int batchSize = 10_000;
        int totalUsers = 1_000_000;
        List<User> users = new ArrayList<>();

        for (int i = 1; i <= totalUsers; i++) {
            String nickname = "user_" + UUID.randomUUID();
            User user = new User();
            user.setNickname(nickname);
            user.setEmail(nickname + "@example.com");
            user.setPassword("password123");

            users.add(user);

            if (i % batchSize == 0) {
                userRepository.saveAll(users);
                users.clear();
                System.out.println("Saved " + i + " users");
            }
        }

        if (!users.isEmpty()) {
            userRepository.saveAll(users);
        }

        System.out.println("100만 유저 데이터 생성 완료");
    }
}