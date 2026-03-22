package com.example.hello_spring;

import java.sql.PreparedStatement;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;



@RestController
public class HelloController {

    public HelloController(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    private final JdbcTemplate jdbcTemplate;
    private final Object idLock = new Object();

    @PostMapping("/users")
    public User createUser(@RequestBody User newUser) {
        String sql = "INSERT INTO users(id, name, age) VALUES(?, ?, ?)";

        Long finalId;
        synchronized (idLock) {
            Long nextId = jdbcTemplate.queryForObject("SELECT COALESCE(MAX(id), 0) + 1 FROM users",
                    Long.class);
            finalId = nextId;
        }
        newUser.setId(finalId);

        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(sql);

            ps.setLong(1, newUser.getId());
            ps.setString(2, newUser.getName());
            ps.setInt(3, newUser.getAge());

            return ps;
        });

        return newUser;
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
        String sql = "SELECT id, name, age FROM users WHERE id = ?";

        // User user = jdbcTemplate.queryForObject(sql, new RowMapper<User>() {
        // @Override
        // public User mapRow(ResultSet rs, int rowNum) throws SQLException {

        // User user = new User(rs.getString("name"), rs.getInt("age"));
        // user.setId(rs.getLong("id"));
        // return user;
        // }
        // }, id);
        return jdbcTemplate.queryForObject(sql, (rs, rowNum) -> {
            User user = new User(rs.getString("name"), rs.getInt("age"));
            user.setId(rs.getLong("id"));
            return user;
        }, id);
    }

    // @GetMapping("/users")
    // public List<User> getUserList() {
    // return new ArrayList<>(userStore.values());
    // }
}
