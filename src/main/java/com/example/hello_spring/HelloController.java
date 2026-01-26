package com.example.hello_spring;

import java.util.ArrayList;
import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class HelloController {

    @GetMapping("/hello")
    public String hello() {
        return "Hello World! from Spring Boot";
    }

    @GetMapping("/user")
    public User getUser() {
        User user = new User("test", 20);

        return user;
    }

    @GetMapping("/user/{id}")
    public User getUserById(@PathVariable("id") Long id) {

        return new User("user" + id, id.intValue());
    }


    @GetMapping("/users")
    public List<User> getUserList() {
        List<User> users = new ArrayList<>();
        users.add(new User("Alice", 30));
        users.add(new User("Bob", 25));
        users.add(new User("Charlie", 35));

        return users;
    }



}
