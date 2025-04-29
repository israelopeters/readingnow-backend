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

    public BookPost toBookPost(BookPostDto bookPostDto) {
        BookPost bookPost = new BookPost();

        bookPost.setImageUrl(bookPostDto.getImageUrl());
        bookPost.setBookAuthor(bookPostDto.getBookAuthor());
        bookPost.setUserAuthor(bookPostDto.getUserAuthor());
        bookPost.setBookSummary(bookPostDto.getBookSummary());
        bookPost.setReadingStatus(bookPostDto.getReadingStatus());
        bookPost.setNumberOfPages(bookPostDto.getNumberOfPages());
        bookPost.setDateCreated(bookPostDto.getDateCreated());

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

