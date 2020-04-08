package com.example.demo.service;

import com.example.demo.model.User;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.*;

@Component
public class UserDao {
    private static List<User> users = new ArrayList<>();

    private static int usersCount = 3;

    static {
        users.add(new User(1, "Adam", new Date()));
        users.add(new User(2, "Eve", new Date()));
        users.add(new User(3, "Jack", new Date()));
    }

    public List<User> findAll() {
        return users;
    }

    public User save(User user) {
        if(user.getId() == null) {
            user.setId(++usersCount);
        }
        users.add(user);
        return user;
    }

    public User findOne(int id) {
        for(User user: users) {
            if(user.getId() == id) {
                return user;
            }
        }
        return null;
    }
    //TODO: add exception for updating & deleting user that isnt in the Collection
    public User updateUser(User user) {
        users.stream()
                .filter(e -> e.getId().equals(user.getId()))
                .forEach(e -> {
                    e.setName(user.getName());
                    e.setBirthDate(user.getBirthDate());
                });
        return user;
    }

    public List<User> deleteUser(int id) {
        try {
            /*
            Iterator itr = users.iterator();
            while (itr.hasNext()) {
                User user = (User) itr.next();
                if(user.getId() == id) {
                    itr.remove();
                }
            } */
            // use above method if you are using java version < 8
            users.removeIf(e -> e.getId().equals(id));
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return this.findAll();
    }
}
