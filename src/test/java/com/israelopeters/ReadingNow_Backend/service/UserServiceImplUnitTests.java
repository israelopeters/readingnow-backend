package com.israelopeters.ReadingNow_Backend.service;

import com.israelopeters.ReadingNow_Backend.exception.InvalidUserException;
import com.israelopeters.ReadingNow_Backend.exception.UserAlreadyExistsException;
import com.israelopeters.ReadingNow_Backend.exception.UserNotFoundException;
import com.israelopeters.ReadingNow_Backend.model.Role;
import com.israelopeters.ReadingNow_Backend.model.User;
import com.israelopeters.ReadingNow_Backend.model.dto.DtoMapper;
import com.israelopeters.ReadingNow_Backend.model.dto.UserCreationDto;
import com.israelopeters.ReadingNow_Backend.model.dto.UserDto;
import com.israelopeters.ReadingNow_Backend.repository.RoleRepository;
import com.israelopeters.ReadingNow_Backend.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@DataJpaTest
class UserServiceImplUnitTests {

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
    void getAllUsers_whenUserTableIsEmpty_returnsEmptyList() {
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
    void getAllUsers_whenUserTableIsNotEmpty_returnsListOfPersistedUsers() {
        // Arrange
        User user = new User(1L, "israel@email.com", "password", "Israel",
                "Peters", "@israelpeters", "profile_pic_url", LocalDate.now(), List.of(), List.of());
        UserDto userDto = new UserDto("israel@email.com", "Israel", "Peters",
                "@israelpeters", "profile_pic_url", List.of());

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

    @Test
    @DisplayName("getUserByEmail() throws a UserNotFoundException when email is not in data store")
    void getUserByEmail_whenUserIsNotInDataStore_throwsUserNotFoundException() {
        // Arrange
        when(userRepository.findByEmail("israel@email.com")).thenThrow(UserNotFoundException.class);

        // Act and Assert
        assertThrows(UserNotFoundException.class, () -> userServiceImpl.getUserByEmail("israel@email.com"));
    }

    @Test
    @DisplayName("getUserByEmail() returns a UserDto object when email is in data store")
    void getUserByEmail_whenUserIsInDataStore_returnsUserWithGivenEmail() {
        // Arrange
        User user = new User(1L, "israel@email.com", "password", "Israel",
                "Peters", "@israelpeters","profile_pic_url", LocalDate.now(), List.of(), List.of());
        UserDto userDtoExpected = new UserDto("israel@email.com", "Israel", "Peters",
                "@israelpeters", "profile_pic_url", List.of());

        when(dtoMapper.toUserDto(user)).thenReturn(userDtoExpected);
        when(userRepository.findByEmail("israel@email.com")).thenReturn(Optional.of(user));

        // Act
        UserDto userDtoActual = userServiceImpl.getUserByEmail("israel@email.com");

        // Assert
        assertEquals(userDtoExpected, userDtoActual);
    }

    @Test
    @DisplayName("addUser() throws a UserAlreadyExistsException")
    void addUser_whenUserAlreadyExists_throwsUserAlreadyExistsException() {
        //Arrange
        UserCreationDto userCreationDto = new UserCreationDto(
                "Israel", "Peters", "israel@email.com", "password",
                "@israelpeters", "profile_pic_url");

        when(userRepository.findByEmail(userCreationDto.getEmail()))
                .thenThrow(new UserAlreadyExistsException("User already exists!"));

        //Act and Assert
        assertThrows(UserAlreadyExistsException.class, () -> userServiceImpl.addUser(userCreationDto));
    }

    @Test
    @DisplayName("addUser() throws an InvalidUserException when email field is blank")
    void addUser_whenUserEmailIsBlank_throwsInvalidUserException() {
        //Arrange
        UserCreationDto userCreationDto = new UserCreationDto(
                "Israel", "Peters", "", "password",
                "@israelpeters","profile_pic_url");

        //Act and Assert
        assertThrows(InvalidUserException.class, () -> userServiceImpl.addUser(userCreationDto));
    }

    @Test
    @DisplayName("addUser() throws an InvalidUserException when password field is blank")
    void addUser_whenUserPasswordIsBlank_throwsInvalidUserException() {
        //Arrange
        UserCreationDto userCreationDto = new UserCreationDto(
                "Israel", "Peters", "israel@email.com", "",
                "@israelpeters", "profile_pic_url");

        //Act and Assert
        assertThrows(InvalidUserException.class, () -> userServiceImpl.addUser(userCreationDto));
    }

    @Test
    @DisplayName("addUser() throws an InvalidUserException when first name field is blank")
    void addUser_whenUserFirstNameIsBlank_throwsInvalidUserException() {
        //Arrange
        UserCreationDto userCreationDto = new UserCreationDto(
                " ", "Peters", "israel@email.com", "password",
                "profile_pic_url", "@israelpeters");

        //Act and Assert
        assertThrows(InvalidUserException.class, () -> userServiceImpl.addUser(userCreationDto));
    }

    @Test
    @DisplayName("addUser() throws an InvalidUserException when last name field is blank")
    void addUser_whenUserLastNameIsBlank_throwsInvalidUserException() {
        //Arrange
        UserCreationDto userCreationDto = new UserCreationDto(
                "Israel", " ", "israel@email.com", "password",
                "profile_pic_url", "@israelpeters");

        //Act and Assert
        assertThrows(InvalidUserException.class, () -> userServiceImpl.addUser(userCreationDto));
    }

    @Test
    @DisplayName("addUser() returns userDto mapped from persisted user")
    void addUser_whenUserDoesNotYetExist_returnsUserDtoObjectOfPersistedUser() {
        //Arrange
        User user = new User(1L, "israel@email.com", "password", "Israel",
                "Peters", "@israelpeters", "profile_pic_url", LocalDate.now(), List.of(), List.of());
        UserCreationDto userCreationDto = new UserCreationDto(
                "Israel", "Peters", "israel@email.com", "password",
                "profile_pic_url", "@israelpeters");
        UserDto userDtoExpected = new UserDto("israel@email.com", "Israel", "Peters",
                "@israelpeters", "profile_pic_url", List.of());

        Role role = new Role();
        role.setId(1L);
        role.setName("ROLE_USER");

        // Return an empty Optional because the check for whether user exists must be false for user to be added
        when(userRepository.findByEmail(userCreationDto.getEmail())).thenReturn(Optional.empty());
        when(dtoMapper.toUser(userCreationDto)).thenReturn(user);
        when(passwordEncoder.encode(userCreationDto.getPassword())).thenReturn("encoded_password");
        when(roleRepository.findByName("ROLE_USER")).thenReturn(role);
        when(roleRepository.save(role)).thenReturn(role);
        when(userRepository.save(user)).thenReturn(user);
        when(dtoMapper.toUserDto(user)).thenReturn(userDtoExpected);

        //Act
        UserDto userCreationDtoActual = userServiceImpl.addUser(userCreationDto);

        //Assert
        assertEquals(userDtoExpected, userCreationDtoActual);
    }

    @Test
    @DisplayName(("deleteUser() throws a UserNotFound exception"))
    void deleteUser_whenUserDoesNotExist_throwsUserNotFoundException() {
        //Arrange
        // Return an empty Optional because the check for whether user exists must be false for user to be added
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        //Act and Assert
        assertThrows(UserNotFoundException.class, ()-> userServiceImpl.deleteUserById(1L));
    }

    @Test
    @DisplayName(("deleteUser() calls UserRepository's delete method once"))
    void deleteUser_whenUserExists_callsRepositoryDeleteMethodOnce() {
        //Arrange
        User existingUser = new User(1L, "israel@email.com", "password", "Israel",
                "Peters", "@israelpeters", "profile_pic_url", LocalDate.now(), List.of(), List.of());

        when(userRepository.findById(1L)).thenReturn(Optional.of(existingUser));
        doNothing().when(userRepository).deleteById(isA(Long.class));

        //Act
        userServiceImpl.deleteUserById(1L);

        // Assert
        verify(userRepository, times(1)).deleteById(1L);
    }
}