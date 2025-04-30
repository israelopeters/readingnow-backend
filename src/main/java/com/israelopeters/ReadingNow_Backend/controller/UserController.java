package com.israelopeters.ReadingNow_Backend.controller;

import com.israelopeters.ReadingNow_Backend.model.dto.UserCreationDto;
import com.israelopeters.ReadingNow_Backend.model.dto.UserDto;
import com.israelopeters.ReadingNow_Backend.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
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

    @Tag(name = "get", description = "All GET methods")
    @Operation(summary = "Get all users", description = "Get all saved users")
    @ApiResponse(responseCode = "200", description = "All users found",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDto.class))))
    @GetMapping("/")
    public ResponseEntity<List<UserDto>> getAllUsers() {
        return new ResponseEntity<>(userService.getAllUsers(), HttpStatus.OK);
    }

    @Tag(name = "get", description = "All GET methods")
    @Operation(summary = "Get user by email", description = "Get a user by a particular email")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "User found",
                    content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserDto.class)))),
            @ApiResponse(responseCode = "404", description = "User not found")})
    @GetMapping("/email/{email}")
    public ResponseEntity<UserDto> getUserByEmail(
            @Parameter(description = "Email of user to find", required = true)
            @RequestParam("email") String email) {
        return new ResponseEntity<>(userService.getUserByEmail(email), HttpStatus.OK);
    }


    @Tag(name = "add", description = "All ADD methods")
    @Operation(summary = "Add user", description = "Add a new user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "User created",
                    content = {@Content(mediaType = "application/json",
                            schema = @Schema(implementation = UserCreationDto.class))}),
            @ApiResponse(responseCode = "400", description = "Invalid user!")})
    @PostMapping("/add")
    public ResponseEntity<UserDto> addUser(
            @Parameter(description = "User to add to data store", required = true)
            @RequestBody UserCreationDto userCreationDto) {
        return new ResponseEntity<>(userService.addUser(userCreationDto), HttpStatus.CREATED);
    }


    @Tag(name = "delete", description = "All DELETE methods")
    @Operation(summary = "Delete user", description = "Delete a user by its id")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "User deleted"),
            @ApiResponse(responseCode = "404", description = "User not found")})
    @DeleteMapping("/delete{id}")
    public ResponseEntity<Void> deleteUserById(
            @Parameter(description = "Id of user to delete from data store", required = true)
            @RequestParam("id") Long id) {
        userService.deleteUserById(id);
        return new ResponseEntity<>(HttpStatus.OK);
    }
}
