package com.israelopeters.ReadingNow_Backend.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserCreationDto {

    private String email;

    private String password;

    private String firstName;

    private String lastName;

    private String username;
}

