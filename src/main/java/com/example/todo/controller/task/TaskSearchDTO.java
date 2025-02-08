package com.example.todo.controller.task;

import java.util.List;
import java.util.Optional;

public record TaskSearchDTO(
        String summary,
        List<String> statusList
) {

    //list.htmlの30行目とかに関数を入れて中から引数の値が含まれているかどうか
    public boolean isChecked(String status) {
        return Optional.ofNullable(statusList)
                .map(l -> l.contains(status))
                .orElse(false);
    }
}
