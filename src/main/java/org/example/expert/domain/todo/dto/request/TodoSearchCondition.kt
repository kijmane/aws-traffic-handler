package org.example.expert.domain.todo.dto.request

import java.time.LocalDate

class TodoSearchCondition (
    val titleKeyword: String? = null,
    val managerNickname: String? = null,
    val startDate: LocalDate? = null,
    val endDate: LocalDate? = null,
    val weather: String? = null
)