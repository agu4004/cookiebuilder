package com.example.helloworld.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api")

public class HelloRestController {
    @GetMapping("/hello")
    public Map<String, String> sayHello(@RequestParam(name="name", required=false, defaultValue="World") String name) {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Hello, " + name + "!");
        return response;
    }

}
