package com.example.demo.controller;

import com.example.demo.exception.UserNotFoundException;
import com.example.demo.model.Post;
import com.example.demo.model.User;
import com.example.demo.repository.UserRepo;
import com.example.demo.service.UserDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;
import org.springframework.http.HttpStatus;
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

    @GetMapping({"/",""})
    public List<User> retrieveAllUsers() {
        return userRepo.findAll();
    }

    @PostMapping("/")
    public ResponseEntity<Object> createUser(@RequestBody User user) {
        User savedUser = userRepo.save(user);
        URI location = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(savedUser.getId())
                .toUri();
        return ResponseEntity.created(location).build();
    }

    @GetMapping("/{id}")
    public EntityModel<User> retrieveUser(@PathVariable int id) {
        Optional<User> user = userRepo.findById(id);
        if(!user.isPresent())
            throw new UserNotFoundException("id-"+id);

        EntityModel<User> model = new EntityModel<>(user.get());
        WebMvcLinkBuilder linkTo = WebMvcLinkBuilder
                .linkTo(
                        WebMvcLinkBuilder
                                .methodOn(this.getClass())
                                .retrieveAllUsers());
        model.add(linkTo.withRel("all-users"));
        return model;
    }

    @PutMapping("/{id}")
    public EntityModel<User> updateUser(@PathVariable int id, @RequestBody User user) {
        if( user == null )
            throw new UserNotFoundException("Not Found");

        User updatedUser = userService.updateUser(user);
        EntityModel<User> model = new EntityModel<>(user);
        WebMvcLinkBuilder linkTo = WebMvcLinkBuilder
                .linkTo(
                        WebMvcLinkBuilder
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
    public List<Post> retrieveAllUsers(@PathVariable int id) {
        Optional<User> userOptional = userRepo.findById(id);
        if(!userOptional.isPresent())
            throw new UserNotFoundException("id-" +id);

        return userOptional.get().getPosts();
    }

}
