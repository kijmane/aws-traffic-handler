package org.example.expert.domain.todo.repository

import org.example.expert.domain.todo.entity.Todo
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate
import java.util.Optional

interface TodoRepository : JpaRepository<Todo, Long> {

    @Query(
        """
        SELECT t FROM Todo t 
        LEFT JOIN FETCH t.user u 
        ORDER BY t.modifiedAt DESC
        """
    )
    // 전체 Todo를 user와 함께 fetchjoin으로 조회 + 최신 수정순 정렬 (페이징 지원)
    fun findAllByOrderByModifiedAtDesc(pageable: Pageable): Page<Todo>

    @Query(
        """
        SELECT t FROM Todo t 
        LEFT JOIN t.user 
        WHERE t.id = :todoId
        """
    )
    // ID를 기준으로 Todo 조회 (user 연관 엔티티까지 함께 조회)
    fun findByIdWithUser(@Param("todoId") todoId: Long): Optional<Todo>

    @Query(
        """
        SELECT t FROM Todo t 
        LEFT JOIN t.user u 
        WHERE (:titleKeyword IS NULL OR t.title LIKE %:titleKeyword%) 
        AND (:managerNickname IS NULL OR u.nickname LIKE %:managerNickname%) 
        AND (:startDate IS NULL OR t.modifiedAt >= :startDate) 
        AND (:endDate IS NULL OR t.modifiedAt <= :endDate) 
        AND (:weather IS NULL OR t.weather LIKE %:weather%) 
        ORDER BY t.modifiedAt DESC
        """
    )
    // 조건에 따라 동적 검색 수행
    fun searchTodos(
        @Param("titleKeyword") titleKeyword: String?,
        @Param("managerNickname") managerNickname: String?,
        @Param("startDate") startDate: LocalDate?,
        @Param("endDate") endDate: LocalDate?,
        @Param("weather") weather: String?,
        pageable: Pageable
    ): Page<Todo>
}