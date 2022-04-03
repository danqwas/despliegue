package com.afterdrawing.backendapi.controller;

import com.afterdrawing.backendapi.core.entity.User;
import com.afterdrawing.backendapi.core.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api")
public class UserController {
    @Autowired
    public UserService userService;

    @GetMapping("/users")
    public ResponseEntity<List<User>> findAllUsers(){
        try {
            List<User> users = userService.getAllUsers();
            if (!users.isEmpty())
                return ResponseEntity.ok(users);
            else
                return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @GetMapping(value = "/users/{userId}")
    public ResponseEntity<User> findUserById(@PathVariable("userId") Long userId) {
        try {
            Optional<User> user = userService.getUserById(userId);
            return user.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @PostMapping("/users")
    public ResponseEntity<User> insertUser( @RequestBody User user) {
        try {
            User userNew = userService.saveUser(user);
            return ResponseEntity.status(HttpStatus.CREATED).body(userNew);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
    @PutMapping(value = "/users/{userId}")
    public ResponseEntity<User> updateUser(
            @PathVariable("userId") Long userId, @Valid @RequestBody User user) {
        User user1 = userService.updateUser(userId, user);
        return  ResponseEntity.ok(user1);
    }
    @DeleteMapping(value = "/users/{userId}")
    public ResponseEntity<User> deleteUser(@PathVariable("userId") Long userId) {
        try{
            Optional<User> userDelete = userService.getUserById(userId);
            if (userDelete.isEmpty()) {
                return ResponseEntity.notFound().build();
            }
            userService.deleteUser(userId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }
}
