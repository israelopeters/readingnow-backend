package com.israelopeters.ReadingNow_Backend.controller;

import com.israelopeters.ReadingNow_Backend.model.dto.UserCreationDto;
import com.israelopeters.ReadingNow_Backend.model.dto.UserDto;
import com.israelopeters.ReadingNow_Backend.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("api/v1/users")
public class UserController {

    @Autowired
    UserService userService;

    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @GetMapping("/{email}")
    public ResponseEntity<UserDto> getUserByEmail(@RequestParam("email") String email) {
        return new ResponseEntity<>(userService.getUserByEmail(email), HttpStatus.OK);
    }

    @PostMapping("/add")
    public ResponseEntity<UserDto> addUser(@RequestBody UserCreationDto userCreationDto) {
        return new ResponseEntity<>(userService.addUser(userCreationDto), HttpStatus.CREATED);
    }

    @DeleteMapping("/delete{id}")
    public ResponseEntity<Void> deleteUserById(@RequestParam("id") Long id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
