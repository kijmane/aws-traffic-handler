package org.example.expert.domain.todo.repository

import org.example.expert.domain.todo.entity.Log
import org.springframework.data.jpa.repository.JpaRepository

interface LogRepository : JpaRepository<Log, Long> {
}