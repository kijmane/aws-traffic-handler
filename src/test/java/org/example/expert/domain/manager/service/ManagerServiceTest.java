package org.example.expert.domain.manager.service;

import jakarta.transaction.Transactional;
import org.example.expert.domain.user.entity.User;
import org.example.expert.domain.common.dto.AuthUser;
import org.example.expert.domain.manager.dto.request.ManagerSaveRequest;
import org.example.expert.domain.todo.entity.Log;
import org.example.expert.domain.todo.repository.LogRepository;
import org.example.expert.domain.todo.repository.TodoRepository;
import org.example.expert.domain.user.enums.UserRole;
import org.example.expert.domain.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ManagerServiceTest {

    @Autowired private ManagerService managerService;
    @Autowired private LogRepository logRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private TodoRepository todoRepository;

    @Test
    void 매니저등록_실패해도_로그_저장_해주세요_부탁_드려요() {
        User savedUser = userRepository.save(
                new User("testuser", "test@example.com", "password123", UserRole.USER)
        );

        AuthUser authUser = new AuthUser(savedUser.getId(), savedUser.getEmail(), savedUser.getUserRole());

        Todo todo = new Todo("테스트 일정", "내용", "맑음", savedUser);
        todoRepository.save(todo);

        ManagerSaveRequest request = new ManagerSaveRequest(99999L);

        try {
            managerService.saveManager(authUser, todo.getId(), request);
        } catch (Exception e) {
            e.printStackTrace();
        }

        List<Log> logs = logRepository.findAll();
        assertFalse(logs.isEmpty(), "로그가 저장되지 않았습니다!");

        Log savedLog = logs.get(0);
        System.out.println("로그 내용: " + savedLog.getMessage() + ", 요청자: " + savedLog.getRequestUser());

        assertEquals("매니저 등록 요청", savedLog.getMessage());
    }
}