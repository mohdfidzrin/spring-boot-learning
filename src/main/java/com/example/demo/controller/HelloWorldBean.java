package com.example.demo.controller;

public class HelloWorldBean {
    private String message;

    public HelloWorldBean() {
    }

    public HelloWorldBean(String hello_world_bean) {
        this.message = hello_world_bean;
    }

    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "HelloWorldBean{" +
                "message='" + message + '\'' +
                '}';
    }
}
