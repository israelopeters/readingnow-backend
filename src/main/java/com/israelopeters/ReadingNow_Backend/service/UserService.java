package com.israelopeters.ReadingNow_Backend.service;

import com.israelopeters.ReadingNow_Backend.model.dto.UserCreationDto;
import com.israelopeters.ReadingNow_Backend.model.dto.UserDto;

import java.util.HashMap;
import java.util.List;

public interface UserService {
    List<UserDto> getAllUsers();
    UserDto getUserByEmail(String email);
    UserDto addUser(UserCreationDto userCreationDto);
    void deleteUserById(Long id);
}
