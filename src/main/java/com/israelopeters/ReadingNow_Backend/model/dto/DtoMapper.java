package com.israelopeters.ReadingNow_Backend.model.dto;

import com.israelopeters.ReadingNow_Backend.model.User;
import org.springframework.stereotype.Component;

@Component
public class DtoMapper {

    public User toUser(UserCreationDto userCreationDto) {
        User user = new User();
        user.setFirstName(userCreationDto.getFirstName());
        user.setLastName(userCreationDto.getLastName());
        user.setEmail(userCreationDto.getEmail());
        user.setPassword(userCreationDto.getPassword());

        if (userCreationDto.getUsername() == null) {
            user.setUsername("@" + userCreationDto.getFirstName() + userCreationDto.getLastName());
        }
        return user;
    }

    public UserDto toUserDto(User user) {
        UserDto userDto = new UserDto();
        userDto.setFirstName(user.getFirstName());
        userDto.setLastName(user.getLastName());
        userDto.setEmail(user.getEmail());
        userDto.setUsername(user.getUsername());
        userDto.setRoles(user.getRoles());
        return userDto;
    }
}

