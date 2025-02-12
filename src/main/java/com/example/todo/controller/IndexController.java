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
        //メモ：Controllerの/の後にreturn後の内容が続く、localhost8080に接続したときにindex.htmlにアクセス
    }
}
