package com.israelopeters.ReadingNow_Backend.controller;

import com.israelopeters.ReadingNow_Backend.model.dto.UserDto;
import com.israelopeters.ReadingNow_Backend.service.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.when;

@AutoConfigureMockMvc
@SpringBootTest
class UserControllerTest {

    @Mock
    private UserServiceImpl userServiceImpl;

    @InjectMocks
    private UserController userController;

    @Autowired
    MockMvc mockMvcController;

    @BeforeEach
    public void setup() {
        mockMvcController = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    @DisplayName("getAllUsers() returns an empty list and OK status")
    void getAllUsers_whenUserTableIsEmpty_ReturnsAnEmptyListAndOkStatusCode() throws Exception {
        // Arrange
        List<UserDto> userDtoList =  List.of();
        when(userServiceImpl.getAllUsers()).thenReturn(userDtoList);

        // Act and assert
        this.mockMvcController.perform(MockMvcRequestBuilders.get("/api/v1/users/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(0));
    }

    @Test
    @DisplayName("getAllUsers() returns a list of UserDto objects and OK status")
    void getAllUsers_whenUserTableIsNotEmpty_ReturnsAListOfUserDtosAndOkStatusCode() throws Exception {
        // Arrange
        UserDto userDtoOne = new UserDto("israel@email.com", "Israel", "Peters",
                "@israelpeters", List.of());
        UserDto userDtoTwo = new UserDto("human@email.com", "Human", "Being",
                "@humanbeing", List.of());
        List<UserDto> userDtoList =  List.of(userDtoOne, userDtoTwo);
        when(userServiceImpl.getAllUsers()).thenReturn(userDtoList);

        // Act and assert
        this.mockMvcController.perform(MockMvcRequestBuilders.get("/api/v1/users/"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2));
    }
}