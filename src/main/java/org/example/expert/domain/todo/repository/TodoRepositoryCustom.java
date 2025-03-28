package org.example.expert.domain.todo.repository;

import org.springframework.data.domain.Page;

import org.springframework.data.domain.Pageable;

public interface TodoRepositoryCustom {
    Page<TodoSearchResult> searchTodos(TodoSearchCondition condition, Pageable pageable);
}
