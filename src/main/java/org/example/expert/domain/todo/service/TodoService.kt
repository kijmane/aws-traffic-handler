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
import org.example.expert.domain.user.repository.UserRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageImpl
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
@Transactional(readOnly = true)
class TodoService(
    private val todoRepository: TodoRepository,
    private val userRepository: UserRepository,
    private val weatherClient: WeatherClient
) {

    @Transactional
    fun saveTodo(authUser: AuthUser, todoSaveRequest: TodoSaveRequest): TodoSaveResponse {
        val user = userRepository.findByEmail(authUser.email)
            .orElseThrow { InvalidRequestException("존재하지 않는 사용자입니다.") }

        val weather = weatherClient.getTodayWeather()

        val newTodo = Todo(
            title = todoSaveRequest.title,
            contents = todoSaveRequest.contents,
            weather = weather,
            user = user
        )

        val savedTodo = todoRepository.save(newTodo)

        return TodoSaveResponse(
            savedTodo.id ?: throw IllegalStateException("Todo ID가 null입니다."),
            savedTodo.title,
            savedTodo.contents,
            savedTodo.weather,
            UserResponse(
                user.id ?: throw IllegalStateException("User ID가 null입니다."),
                user.email
            )
        )
    }

    fun getTodos(page: Int, size: Int): Page<TodoResponse> {
        val pageable: Pageable = PageRequest.of(page - 1, size)
        val todos = todoRepository.findAllByOrderByModifiedAtDesc(pageable)

        val responses = todos.map { todo ->
            TodoResponse(
                todo.id ?: throw IllegalStateException("Todo ID가 null입니다."),
                todo.title,
                todo.contents,
                todo.weather,
                UserResponse(
                    todo.user.id ?: throw IllegalStateException("User ID가 null입니다."),
                    todo.user.email
                ),
                todo.createdAt,
                todo.modifiedAt
            )
        }

        return PageImpl(responses.content, pageable, todos.totalElements)
    }

    fun getTodo(todoId: Long): TodoResponse {
        val todo = todoRepository.findByIdWithUser(todoId)
            .orElseThrow { InvalidRequestException("요청하신 Todo를 찾을 수 없습니다.") }

        return TodoResponse(
            todo.id ?: throw IllegalStateException("Todo ID가 null입니다."),
            todo.title,
            todo.contents,
            todo.weather,
            UserResponse(
                todo.user.id ?: throw IllegalStateException("User ID가 null입니다."),
                todo.user.email
            ),
            todo.createdAt,
            todo.modifiedAt
        )
    }

    fun searchTodos(condition: TodoSearchCondition, pageable: Pageable): Page<TodoSearchResult> {
        return todoRepository.searchTodos(condition, pageable)
    }
}
