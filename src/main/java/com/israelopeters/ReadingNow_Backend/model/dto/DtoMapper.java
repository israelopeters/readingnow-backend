package com.israelopeters.ReadingNow_Backend.model.dto;

import com.israelopeters.ReadingNow_Backend.model.BookPost;
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
            user.setUsername("@" +
                    userCreationDto.getFirstName().toLowerCase() +
                    userCreationDto.getLastName().toLowerCase());
        } else {
            user.setUsername("@" + userCreationDto.getUsername());
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

    public BookPost toBookPost(BookPostCreationDto bookPostCreationDto) {
        BookPost bookPost = new BookPost();

        bookPost.setImageUrl(bookPostCreationDto.getImageUrl());
        bookPost.setBookAuthor(bookPostCreationDto.getBookAuthor());
        bookPost.setBookSummary(bookPostCreationDto.getBookSummary());
        bookPost.setReadingStatus(bookPostCreationDto.getReadingStatus());
        bookPost.setNumberOfPages(bookPostCreationDto.getNumberOfPages());

        return bookPost;
    }

    public BookPostDto toBookPostDto(BookPost bookPost) {
        BookPostDto bookPostDto = new BookPostDto();

        bookPostDto.setImageUrl(bookPost.getImageUrl());
        bookPostDto.setBookAuthor(bookPost.getBookAuthor());
        bookPostDto.setUserAuthor(bookPost.getUserAuthor());
        bookPostDto.setBookSummary(bookPost.getBookSummary());
        bookPostDto.setReadingStatus(bookPost.getReadingStatus());
        bookPostDto.setNumberOfPages(bookPost.getNumberOfPages());
        bookPostDto.setDateCreated(bookPost.getDateCreated());

        return bookPostDto;
    }
}

