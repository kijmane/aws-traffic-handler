package org.example.expert.domain.todo.repository;

import org.example.expert.domain.todo.entity.Todo;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.Optional;

interface TodoRepository extends JpaRepository<Todo, Long>, TodoRepositoryCustom {

    @Query("SELECT t FROM Todo t LEFT JOIN FETCH t.user u ORDER BY t.modifiedAt DESC")
    Page<Todo> findAllByOrderByModifiedAtDesc(Pageable pageable);

    @Query("SELECT t FROM Todo t " +
            "LEFT JOIN t.user " +
            "WHERE t.id = :todoId")
    Optional<Todo> findByIdWithUser(@Param("todoId") Long todoId);

    @Query("SELECT t FROM Todo t " +
            "LEFT JOIN t.user u " +
            "WHERE (:titleKeyword IS NULL OR t.title LIKE %:titleKeyword%) " +
            "AND (:managerNickname IS NULL OR u.nickname LIKE %:managerNickname%) " +
            "AND (:startDate IS NULL OR t.modifiedAt >= :startDate) " +
            "AND (:endDate IS NULL OR t.modifiedAt <= :endDate) " +
            "AND (:weather IS NULL OR t.weather LIKE %:weather%) " +
            "ORDER BY t.modifiedAt DESC")
    Page<Todo> searchTodos(@Param("titleKeyword") String titleKeyword,
                           @Param("managerNickname") String managerNickname,
                           @Param("startDate") LocalDate startDate,
                           @Param("endDate") LocalDate endDate,
                           @Param("weather") String weather,
                           Pageable pageable);
}
