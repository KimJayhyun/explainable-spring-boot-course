package com.example.hello_spring;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class HelloController {

    public HelloController(UserRepository userRepository, JdbcTemplate jdbcTemplate) {
        this.userRepository = userRepository;
    }

    private final UserRepository userRepository;

    @PostMapping("/users")
    public User createUser(@RequestBody User newUser) {
        User savedUser = userRepository.save(newUser);
        return savedUser;
    }

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
        return userRepository.findById(id).orElse(null);
    }

    // @GetMapping("/users")
    // public List<User> getUserList() {
    // return new ArrayList<>(userStore.values());
    // }
}
