package org.example.expert.domain.todo.repository;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.querydsl.core.types.Projections;
import com.querydsl.jpa.JPQLQuery;
import org.example.expert.domain.todo.entity.QTodo;
import org.example.expert.domain.comment.entity.QComment;
import org.example.expert.domain.manager.entity.QManager;
import org.example.expert.domain.user.entity.QUser;
import org.springframework.data.domain.*;
import org.springframework.util.StringUtils;

import jakarta.persistence.EntityManager;
import java.time.LocalDate;
import java.util.List;

public class TodoRepositoryImpl implements TodoRepositoryCustom {

    private final JPAQueryFactory queryFactory;

    public TodoRepositoryImpl(EntityManager em) {
        this.queryFactory = new JPAQueryFactory(em);
    }

    @Override
    public Page<TodoSearchResult> searchTodos(TodoSearchCondition condition, Pageable pageable) {
        QTodo todo = QTodo.todo;
        QManager manager = QManager.manager;
        QComment comment = QComment.comment;
        QUser user = QUser.user;

        JPQLQuery<TodoSearchResult> query = queryFactory
                .select(Projections.constructor(
                        TodoSearchResult.class,
                        todo.title,
                        manager.countDistinct(),
                        comment.countDistinct()
                ))
                .from(todo)
                .leftJoin(todo.managers, manager)
                .leftJoin(todo.comments, comment)
                .leftJoin(manager.user, user)
                .where(
                        titleContains(condition.getTitleKeyword()),
                        nicknameContains(condition.getManagerNickname()),
                        createdDateBetween(condition.getStartDate(), condition.getEndDate())
                )
                .groupBy(todo.id)
                .orderBy(todo.createdAt.desc());

        List<TodoSearchResult> content = query
                .offset(pageable.getOffset())
                .limit(pageable.getPageSize())
                .fetch();

        Long total = queryFactory
                .select(todo.countDistinct())
                .from(todo)
                .leftJoin(todo.managers, manager)
                .leftJoin(manager.user, user)
                .where(
                        titleContains(condition.getTitleKeyword()),
                        nicknameContains(condition.getTitleKeyword()),
                        createdDateBetween(condition.getStartDate(), condition.getEndDate())
                )
                .fetchOne();

        return new PageImpl<>(content, pageable, total != null ? total : 0);
    }

    private BooleanExpression titleContains(String keyword) {
        return StringUtils.hasText(keyword) ? QTodo.todo.title.containsIgnoreCase(keyword) : null;
    }

    private BooleanExpression nicknameContains(String nickname) {
        return StringUtils.hasText(nickname) ? QUser.user.nickname.containsIgnoreCase(nickname) : null;
    }

    private BooleanExpression createdDateBetween(LocalDate start, LocalDate end) {
        if (start == null || end == null) return null;
        return QTodo.todo.createdAt.between(start.atStartOfDay(), end.plusDays(1).atStartOfDay());
    }
}