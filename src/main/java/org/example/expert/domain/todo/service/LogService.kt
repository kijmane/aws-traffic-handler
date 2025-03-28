package org.example.expert.domain.todo.service

import org.example.expert.domain.todo.entity.Log
import org.example.expert.domain.todo.repository.LogRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Propagation
import org.springframework.transaction.annotation.Transactional

@Service
class LogService (
    private val logRepository: LogRepository
) {
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    fun saveLog(message: String, requestIp: String, requestUser: String){
        val log = Log(message, requestIp, requestUser)
        logRepository.save(log)
    }
}