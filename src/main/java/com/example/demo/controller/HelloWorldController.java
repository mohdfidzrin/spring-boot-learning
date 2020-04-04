package com.example.demo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HelloWorldController {

    @GetMapping("/hello-world")
    public String helloWorld() {
        return "Hello World 2";
    }

    @GetMapping("/hello-world-bean")
    public HelloWorldBean helloWorldBean() {
        return new HelloWorldBean("Hello World Bean");
    }

    @GetMapping("/hello-world/path/{name}")
    public HelloWorldBean pathVar(@PathVariable String name) {
        return new HelloWorldBean(String.format("Hello World, %s", name));
    }
}
