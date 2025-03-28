package org.example.expert.domain.manager.service;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.manager.config.TestS3Config;
import org.example.expert.domain.manager.dto.request.ManagerSaveRequest;
import org.example.expert.domain.todo.entity.Todo;
import org.example.expert.domain.todo.repository.LogRepository;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@SpringBootTest
@Import(TestS3Config.class)
class ManagerServiceTest {

    private static final int MAX_COUNT = 1_000_000;

    @Autowired private ManagerService managerService;
    @Autowired private LogRepository logRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private TodoRepository todoRepository;

    @PersistenceContext
    private EntityManager entityManager;

    @Test
    @Transactional
    @Rollback(false)
    void MangerServiceTest() {
        long start = System.currentTimeMillis();

        User user = userRepository.save(new User("nickname", "test@example.com", "password", UserRole.USER));
        Todo todo = todoRepository.save(new Todo("테스트 일정", "내용", "좋음", user));
        AuthUser authUser = new AuthUser(user.getId(), user.getEmail(), user.getUserRole());

        List<ManagerSaveRequest> requests = new ArrayList<>();

        for (int i = 0; i < MAX_COUNT; i++) {
            requests.add(new ManagerSaveRequest(generateRandomUserId()));

            if (requests.size() == 1000) {
                for (ManagerSaveRequest request : requests) {
                    try {
                        managerService.saveManager(authUser, todo.getId(), request);
                    } catch (Exception ignored) {
                    }
                }
                entityManager.flush();
                entityManager.clear();
                requests.clear();
            }
        }

        for (ManagerSaveRequest request : requests) {
            try {
                managerService.saveManager(authUser, todo.getId(), request);
            } catch (Exception ignored) {
            }
        }

        entityManager.flush();
        entityManager.clear();

        long totalLogs = logRepository.count();
        long end = System.currentTimeMillis();

        System.out.println("총 저장된 로그 수: " + totalLogs + ", 걸린 시간(ms): " + (end - start));
    }

    private Long generateRandomUserId() {
        return Math.abs(UUID.randomUUID().getMostSignificantBits() % 1_000_000);
    }
}