package com.example.todo.controller.task;

import com.example.todo.service.task.TaskSearchEntity;
import com.example.todo.service.task.TaskStatus;

import java.util.List;
import java.util.Optional;

public record TaskSeacrhForm(
        String summary,
        List<String> status
) {
    public TaskSearchEntity toEntity() {
        //searchForm.status() == null: List.of() 107えぐい
        //searchForm.status() != null: List<TaskStatus>
        var statusEntityList = Optional.ofNullable(status())
                .map(statusList -> statusList.stream()
                        .map(TaskStatus::valueOf)
                        .toList())
                .orElse(List.of());

        return new TaskSearchEntity(summary(), statusEntityList);
    }

    public TaskSearchDTO toDTO() {
        return new TaskSearchDTO(summary(), status());
    }
}
