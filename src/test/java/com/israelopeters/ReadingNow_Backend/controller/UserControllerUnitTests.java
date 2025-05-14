package com.israelopeters.ReadingNow_Backend.controller;

import com.israelopeters.ReadingNow_Backend.exception.UserNotFoundException;
import com.israelopeters.ReadingNow_Backend.model.dto.UserDto;
import com.israelopeters.ReadingNow_Backend.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.util.AssertionErrors.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ExtendWith(SpringExtension.class)
class UserControllerUnitTests {

    @Autowired
    private MockMvc mockMvcController;

    @MockitoBean
    private UserService userService;

    @Test
    @DisplayName("GET / returns an empty list and OK status")
    @WithMockUser
    void baseEndpoint_whenUserTableIsEmpty_ReturnsAnEmptyListAndOkStatusCode() throws Exception {
        // Arrange
        List<UserDto> userDtoList =  List.of();
        when(userService.getAllUsers()).thenReturn(userDtoList);

        // Act and assert
        this.mockMvcController.perform(get("/api/v1/users/"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(0));
    }

    @Test
    @DisplayName("GET / returns a list of UserDto objects and OK status")
    @WithMockUser
    void baseEndpoint_whenUserTableIsNotEmpty_ReturnsAListOfUserDtosAndOkStatusCode() throws Exception {
        // Arrange
        UserDto userDtoOne = new UserDto("israel@email.com", "Israel", "Peters",
                "@israelpeters", "profile_pic_url", List.of());
        UserDto userDtoTwo = new UserDto("human@email.com", "Human", "Being",
                "@humanbeing", "profile_pic_url", List.of());
        List<UserDto> userDtoList =  List.of(userDtoOne, userDtoTwo);
        when(userService.getAllUsers()).thenReturn(userDtoList);

        // Act and assert
        this.mockMvcController.perform(get("/api/v1/users/"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2));
    }

    @Test
    @DisplayName("GET /email?email={email} returns a 404 status code")
    @WithMockUser
    void emailEndpoint_whenUserIsNotFound_ReturnsA404StatusCode() throws Exception {
        // Arrange
        when(userService.getUserByEmail("israel@email.com"))
                .thenThrow(new UserNotFoundException("User does not exist!"));

        // Act and assert
        this.mockMvcController.perform(get("/api/v1/users/email?email=israel@email.com"))
                .andExpect(status().isNotFound())
                .andExpect(result -> assertEquals(
                        null, "User does not exist!", result.getResolvedException().getMessage()));
    }

    @Test
    @DisplayName("GET /email?email=israel@email.com returns a user and 200 status code")
    @WithMockUser
    void emailEndpoint_whenUserIsFound_ReturnsAUserAnd200StatusCode() throws Exception {
        // Arrange
        UserDto testUser = new UserDto("israel@email.com", "Israel", "Peters",
                "@israelpeters", "profile_pic_url", List.of());
        when(userService.getUserByEmail("israel@email.com")).thenReturn(testUser);

        // Act and assert
        this.mockMvcController.perform(get("/api/v1/users/email?email=israel@email.com"))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers
                        .jsonPath("$.username").value("@israelpeters"));
    }
}