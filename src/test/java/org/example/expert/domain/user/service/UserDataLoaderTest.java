package org.example.expert.domain.user.service;

import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
@SpringBootTest
public class UserDataLoaderTest {

    @Autowired
    private UserDataLoader userDataLoader;

    @Autowired
    private UserRepository userRepository;

    @Test
    public void testLoadUsers() throws Exception {
        userDataLoader.run();
        long userCount = userRepository.count();
        assertEquals(1000000, userCount);
    }
}