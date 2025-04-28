package com.israelopeters.ReadingNow_Backend.service;

import com.israelopeters.ReadingNow_Backend.model.User;
import com.israelopeters.ReadingNow_Backend.model.dto.DtoMapper;
import com.israelopeters.ReadingNow_Backend.model.dto.UserDto;
import com.israelopeters.ReadingNow_Backend.repository.RoleRepository;
import com.israelopeters.ReadingNow_Backend.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class UserServiceImplTest {

    @Mock
    UserRepository userRepository;

    @Mock
    RoleRepository roleRepository;

    @Mock
    DtoMapper dtoMapper;

    @Mock
    PasswordEncoder passwordEncoder;

    @InjectMocks
    UserServiceImpl userServiceImpl;

    @Test
    @DisplayName("getAllUsers() returns empty list")
    void getAllUsersWhenUserTableIsEmpty() {
        // Arrange
        List<User> userListRepository = List.of();
        List<UserDto> userListExpected = List.of();
        when(userRepository.findAll()).thenReturn(userListRepository);

        // Act
        List<UserDto> userListActual = userServiceImpl.getAllUsers();

        // Assert
        assertEquals(userListExpected, userListActual);

    }

    @Test
    @DisplayName("getAllUsers() returns a non-empty list")
    void getAllUsersWhenUserTableIsNotEmpty() {
        // Arrange
        User user = new User(1L, "israel@email.com", "password", "Israel",
                "Peters", "@israelpeters", LocalDate.now(), List.of(), List.of());
        UserDto userDto = new UserDto("israel@email.com", "Israel", "Peters",
                "@israelpeters", List.of());

        List<User> userListRepository = new ArrayList<>();
        userListRepository.add(user);

        List<UserDto> userListExpected = new ArrayList<>();
        userListExpected.add(userDto);

        when(userRepository.findAll()).thenReturn(userListRepository);
        when(dtoMapper.toUserDto(user)).thenReturn(userDto);

        //Act
        List<UserDto> userListActual = userServiceImpl.getAllUsers();

        //Assert
        assertEquals(1, userListActual.size());
        assertEquals("Israel", userListActual.getFirst().getFirstName());
        assertEquals(userListExpected, userListActual);
    }
}