package org.example.expert.domain.todo.controller

import jakarta.validation.Valid
import org.example.expert.client.WeatherClient
import org.example.expert.domain.common.annotation.Auth
import org.example.expert.domain.common.dto.AuthUser
import org.example.expert.domain.todo.dto.request.TodoSaveRequest
import org.example.expert.domain.todo.dto.request.TodoSearchCondition
import org.example.expert.domain.todo.dto.response.TodoResponse
import org.example.expert.domain.todo.dto.response.TodoSaveResponse
import org.example.expert.domain.todo.dto.response.TodoSearchResult
import org.example.expert.domain.todo.service.TodoService
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDate

@RestController
@RequestMapping("/todos")
class TodoController(
    private val todoService: TodoService,
    private val weatherClient: WeatherClient
) {

    @PostMapping
    fun saveTodo(
        @Auth authUser: AuthUser,
        @Valid @RequestBody todoSaveRequest: TodoSaveRequest
    ): ResponseEntity<TodoSaveResponse> {
        val result = todoService.saveTodo(authUser, todoSaveRequest)
        return ResponseEntity.ok(result)
    }

    @GetMapping
    fun getTodos(
        @RequestParam(defaultValue = "1") page: Int,
        @RequestParam(defaultValue = "10") size: Int
    ): ResponseEntity<Page<TodoResponse>> {
        return ResponseEntity.ok(todoService.getTodos(page, size))
    }

    @GetMapping("/{todoId}")
    fun getTodo(@PathVariable todoId: Long): ResponseEntity<TodoResponse> {
        return ResponseEntity.ok(todoService.getTodo(todoId))
    }

    @GetMapping("/search")
    fun searchTodos(
        @RequestParam(required = false) titleKeyword: String?,
        @RequestParam(required = false) managerNickname: String?,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") startDate: LocalDate?,
        @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd") endDate: LocalDate?,
        @RequestParam(required = false) weather: String?,
        @PageableDefault(size = 10, page = 0) pageable: Pageable
    ): ResponseEntity<Page<TodoSearchResult>> {
        val condition = TodoSearchCondition(
            titleKeyword = titleKeyword,
            managerNickname = managerNickname,
            startDate = startDate,
            endDate = endDate,
            weather = weather
        )
        return ResponseEntity.ok(todoService.searchTodos(condition, pageable))
    }
}