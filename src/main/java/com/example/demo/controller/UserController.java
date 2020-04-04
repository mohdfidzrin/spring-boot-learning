package com.example.demo.controller;

import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.User;
import com.example.demo.service.UserDao;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("users")
public class UserController {

    @Autowired
    private UserDao userService;

    @GetMapping("/")
    public List<User> retrieveAllUsers() {
        return userService.findAll();
    }

    @PostMapping("/")
    public ResponseEntity<Object> createUser(@RequestBody User user) {
        User savedUser = userService.save(user);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public User retrieveUser(@PathVariable int id) {
        User user = userService.findOne(id);
        if( user == null )
            throw new UserNotFoundException("id-"+id);
        return user;
    }


}
