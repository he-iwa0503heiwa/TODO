package com.example.todo.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller("/")
public class IndexController {
    // http:local:8080/ -> "hello, world!"
    // Get / -> "Hello, world!"
    @GetMapping
    public String index() {
        return "index";
        //メモ：Controllerの/の後に？的な、localhost8080に接続したときに戻り値(elloworld)にアクセス
    }
}
