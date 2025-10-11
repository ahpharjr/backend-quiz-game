package com.ahphar.backend_quiz_game.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @GetMapping("/testconnection")
    public String testBackend() {
        return "Connected to Backend";
    }
}
