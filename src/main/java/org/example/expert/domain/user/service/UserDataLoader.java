package org.example.expert.domain.user.service;

import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.repository.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class UserDataLoader implements CommandLineRunner {

    private final UserRepository userRepository;

    public UserDataLoader(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public void run(String... args) throws Exception {
        Random random = new Random();
        for (int i = 0; i < 1000000; i++) {
            String nickname = generateRandomNickname();
            User user = new User();
            user.setNickname(nickname);
            user.setEmail(nickname + "@example.com");
            user.setPassword("password123");

            userRepository.save(user);
        }
    }

    private String generateRandomNickname() {
        return "User" + new Random().nextInt(1000000);
    }
}