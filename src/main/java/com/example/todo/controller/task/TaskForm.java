package com.example.todo.controller.task;

import com.example.todo.service.task.TaskEntity;
import com.example.todo.service.task.TaskStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

public record TaskForm(
        @NotBlank//空欄を許さないバリデーション
        @Size(max = 256, message = "256文字以内で入力してください")
        String summary,
        String description,
        @NotBlank//html上では選択できなくてもデベロッパーツールを使って簡単に空欄にできるから信用せずバリデ作っておく
        @Pattern(regexp = "TODO|DOING|DONE", message = "Todo, Doing, Doneのいずれかを選択してください")
        String status
) {
    public static TaskForm fromEntity(TaskEntity taskEntity) {
        return new TaskForm(
                taskEntity.summary(),
                taskEntity.description(),
                taskEntity.status().name()//statusをStringに変換
        );
    }

    public TaskEntity toEntity() {
        return new TaskEntity(null, summary(), description(), TaskStatus.valueOf(status()));
        //↑valueOfでTaskEntity側の型にキャストしてくれる
    }

    //引数にすでにidが入っているパターンのメソッドを用意
    public TaskEntity toEntity(long id) {
        return new TaskEntity(id, summary(), description(), TaskStatus.valueOf(status()));
        //↑valueOfでTaskEntity側の型にキャストしてくれる
    }
}
