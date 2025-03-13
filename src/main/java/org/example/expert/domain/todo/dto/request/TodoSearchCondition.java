package org.example.expert.domain.todo.dto.request;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class TodoSearchCondition {
    private String titleKeyword;
    private String managerNickname;
    private LocalDate startDate;
    private LocalDate endDate;
}