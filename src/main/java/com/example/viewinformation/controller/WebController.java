package com.example.viewinformation.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class WebController {

    @GetMapping({"/", "/{path:[^\\.]*}"})  // 处理根路径和 Vue Router 的 history 模式
    public String index() {
        return "forward:/index.html"; // 这里使用 forward，而不是 return "index"
    }
}

