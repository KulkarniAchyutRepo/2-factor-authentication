package com.app._FactorAuthentication.controller;

import com.app._FactorAuthentication.entity.User;
import com.app._FactorAuthentication.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/users")
public class UserController {
    private UserService userService;
    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/")
    public ResponseEntity<List<User>> findAll(){
        List<User> list = userService.findAll();
        return ResponseEntity.ok(list);
    }

    @PutMapping("/")
    public ResponseEntity<User> update(@RequestBody User user){
        User updatedUser = userService.update(user);
        return ResponseEntity.ok(updatedUser);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteByEmail(@PathVariable long id){
        userService.deleteById(id);
        return ResponseEntity.ok("User deleted.");
    }
}
