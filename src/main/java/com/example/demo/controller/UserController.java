package com.example.demo.controller;

import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.Post;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepo;
import com.example.demo.service.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.mvc.ControllerLinkBuilder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserDao userService;
    @Autowired
    private UserRepo userRepo;

    @CrossOrigin("http://localhost")
    @GetMapping(value = {"/",""}, produces = "application/json")
    public List<User> retrieveAllUsers() {
        return userRepo.findAll();
    }

    @CrossOrigin("http://localhost")
    @PostMapping(value = "/", produces = "application/json")
    public User createUser(@RequestBody User user) {
        User savedUser = userRepo.save(user);
//        URI location = ServletUriComponentsBuilder
//                .fromCurrentRequest()
//                .path("/{id}")
//                .buildAndExpand(savedUser.getId())
//                .toUri();

        return savedUser;
    }

    @CrossOrigin("http://localhost")
    @GetMapping(value = "/{id}", produces = "application/json")
    public Resource<User> retrieveUser(@PathVariable int id) {
        Optional<User> user = userRepo.findById(id);
        if(!user.isPresent())
            throw new UserNotFoundException("id-"+id);

        Resource<User> model = new Resource<>(user.get());
        ControllerLinkBuilder linkTo = ControllerLinkBuilder
                .linkTo(
                        ControllerLinkBuilder
                                .methodOn(this.getClass())
                                .retrieveAllUsers());
        model.add(linkTo.withRel("all-users"));
        return model;
    }

    @PutMapping("/{id}")
    public Resource<User> updateUser(@PathVariable int id, @RequestBody User user) {
        if( user == null )
            throw new UserNotFoundException("Not Found");

        User updatedUser = userService.updateUser(user);
        Resource<User> model = new Resource<>(user);
        ControllerLinkBuilder linkTo = ControllerLinkBuilder
                .linkTo(
                        ControllerLinkBuilder
                                .methodOn(this.getClass())
                                .retrieveAllUsers());
        model.add(linkTo.withRel("all-users"));
        return model;
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable int id) {
        try {
            userRepo.deleteById(id);
        }catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    @GetMapping("/{id}/posts")
    public List<Post> retrieveAllPostByUser(@PathVariable int id) {
        Optional<User> userOptional = userRepo.findById(id);
        if(!userOptional.isPresent())
            throw new UserNotFoundException("id-" +id);

        return userOptional.get().getPosts();
    }

}
