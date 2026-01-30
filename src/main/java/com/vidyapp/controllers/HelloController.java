package com.vidyapp.controllers;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloController {
    @GetMapping("/")
    public String hello() {
        return "Hello World";
    }

    @GetMapping("/hello")
    public String sayHi() {
        return "Hello, Spring Boot!";
    }

    @GetMapping("/contact")
    public String contactPage() {
        return "Contact Page";
    }
}