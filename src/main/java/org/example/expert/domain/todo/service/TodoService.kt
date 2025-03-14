package org.example.expert.domain.todo.service

import org.example.expert.client.WeatherClient
import org.example.expert.domain.common.dto.AuthUser
import org.example.expert.domain.common.exception.InvalidRequestException
import org.example.expert.domain.todo.dto.request.TodoSaveRequest
import org.example.expert.domain.todo.dto.request.TodoSearchCondition
import org.example.expert.domain.todo.dto.response.TodoResponse
import org.example.expert.domain.todo.dto.response.TodoSaveResponse
import org.example.expert.domain.todo.dto.response.TodoSearchResult
import org.example.expert.domain.todo.entity.Todo
import org.example.expert.domain.todo.repository.TodoRepository
import org.example.expert.domain.user.dto.response.UserResponse
import org.example.expert.domain.user.entity.User
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class TodoService(
    private val todoRepository: TodoRepository,
    private val weatherClient: WeatherClient
) {

    @Transactional
    fun saveTodo(authUser: AuthUser, todoSaveRequest: TodoSaveRequest): TodoSaveResponse {
        val user = User.fromAuthUser(authUser)
        val weather = weatherClient.getTodayWeather()

        val newTodo = Todo(
            title = todoSaveRequest.title,
            contents = todoSaveRequest.contents,
            weather = weather,
            user = user
        )

        val savedTodo = todoRepository.save(newTodo)

        return TodoSaveResponse(
            id = requireNotNull(savedTodo.id) { "저장된 Todo의 ID가 null입니다." },
            title = savedTodo.title,
            contents = savedTodo.contents,
            weather = weather,
            user = UserResponse(
                id = requireNotNull(user.id) { "사용자의 ID가 null입니다." },
                email = user.email
            )
        )
    }

    fun getTodos(page: Int, size: Int): Page<TodoResponse> {
        val pageable: Pageable = PageRequest.of(page - 1, size)
        val todos = todoRepository.findAllByOrderByModifiedAtDesc(pageable)

        return todos.map { todo ->
            TodoResponse(
                id = requireNotNull(todo.id),
                title = todo.title,
                contents = todo.contents,
                weather = todo.weather,
                user = UserResponse(
                    id = requireNotNull(todo.user.id),
                    email = todo.user.email
                ),
                createdAt = todo.createdAt,
                modifiedAt = todo.modifiedAt
            )
        }
    }

    fun getTodo(todoId: Long): TodoResponse {
        val todo = todoRepository.findByIdWithUser(todoId)
            .orElseThrow { InvalidRequestException("요청하신 Todo를 찾을 수 없습니다.") }

        val user = todo.user

        return TodoResponse(
            id = requireNotNull(todo.id),
            title = todo.title,
            contents = todo.contents,
            weather = todo.weather,
            user = UserResponse(
                id = requireNotNull(user.id),
                email = user.email
            ),
            createdAt = todo.createdAt,
            modifiedAt = todo.modifiedAt
        )
    }

    fun searchTodos(condition: TodoSearchCondition, pageable: Pageable): Page<TodoSearchResult> {
        return todoRepository.searchTodos(condition, pageable)
    }
}