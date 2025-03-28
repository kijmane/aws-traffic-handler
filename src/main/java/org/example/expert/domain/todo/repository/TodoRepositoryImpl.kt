package org.example.expert.domain.todo.repository

import com.querydsl.core.types.Projections
import com.querydsl.core.types.dsl.BooleanExpression
import com.querydsl.jpa.JPQLQuery
import com.querydsl.jpa.impl.JPAQueryFactory
import org.example.expert.domain.comment.entity.QComment
import org.example.expert.domain.manager.entity.QManager
import org.example.expert.domain.todo.dto.request.TodoSearchCondition
import org.example.expert.domain.todo.dto.response.TodoSearchResult
import org.example.expert.domain.todo.entity.QTodo
import org.example.expert.domain.user.entity.QUser
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.Pageable
import org.springframework.util.StringUtils
import jakarta.persistence.EntityManager
import java.time.LocalDate

class TodoRepositoryImpl(
    em: EntityManager
) : TodoRepositoryCustom {

    private val queryFactory = JPAQueryFactory(em)

    override fun searchTodos(condition: TodoSearchCondition, pageable: Pageable): Page<TodoSearchResult> {
        val todo = QTodo.todo
        val manager = QManager.manager
        val comment = QComment.comment
        val user = QUser.user

        val query: JPQLQuery<TodoSearchResult> = queryFactory
            .select(
                Projections.constructor(
                    TodoSearchResult::class.java,
                    todo.title,
                    manager.countDistinct(),
                    comment.countDistinct()
                )
            )
            .from(todo)
            .leftJoin(todo.managers, manager)
            .leftJoin(todo.comments, comment)
            .leftJoin(manager.user, user)
            .where(
                titleContains(condition.titleKeyword),
                nicknameContains(condition.managerNickname),
                createdDateBetween(condition.startDate, condition.endDate)
            )
            .groupBy(todo.id)
            .orderBy(todo.createdAt.desc())

        val content = query
            .offset(pageable.offset)
            .limit(pageable.pageSize.toLong())
            .fetch()

        val total = queryFactory
            .select(todo.countDistinct())
            .from(todo)
            .leftJoin(todo.managers, manager)
            .leftJoin(manager.user, user)
            .where(
                titleContains(condition.titleKeyword),
                nicknameContains(condition.managerNickname),
                createdDateBetween(condition.startDate, condition.endDate)
            )
            .fetchOne() ?: 0L

        return PageImpl(content, pageable, total)
    }

    private fun titleContains(keyword: String?): BooleanExpression? {
        return if (StringUtils.hasText(keyword)) QTodo.todo.title.containsIgnoreCase(keyword) else null
    }

    private fun nicknameContains(nickname: String?): BooleanExpression? {
        return if (StringUtils.hasText(nickname)) QUser.user.nickname.containsIgnoreCase(nickname) else null
    }

    private fun createdDateBetween(start: LocalDate?, end: LocalDate?): BooleanExpression? {
        if (start == null || end == null) return null
        return QTodo.todo.createdAt.between(start.atStartOfDay(), end.plusDays(1).atStartOfDay())
    }
}