package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.dto.request.TodoSearchCondition;
import org.example.expert.domain.todo.dto.response.TodoSearchResult;
import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

// QueryDSL 기반 커스텀 동적 검색 기능 정의 인터페이스
public interface TodoRepositoryCustom {
    Page<TodoSearchResult> searchTodos(TodoSearchCondition condition, Pageable pageable);
}
