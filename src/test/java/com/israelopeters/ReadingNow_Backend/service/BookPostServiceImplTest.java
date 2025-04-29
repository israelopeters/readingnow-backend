package com.israelopeters.ReadingNow_Backend.service;

import com.israelopeters.ReadingNow_Backend.model.BookPost;
import com.israelopeters.ReadingNow_Backend.model.ReadingStatus;
import com.israelopeters.ReadingNow_Backend.model.User;
import com.israelopeters.ReadingNow_Backend.repository.BookPostRepository;
import com.israelopeters.ReadingNow_Backend.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@SpringBootTest
class BookPostServiceImplTest {

    @Mock
    BookPostRepository bookPostRepository;

    @Mock
    UserRepository userRepository;

    @InjectMocks
    BookPostServiceImpl bookPostService;

    @Test
    @DisplayName("getAllBookPosts() returns an empty list when book post table is empty")
    void getAllBookPostsWhenBookPostTableIsEmpty() {
        //Arrange
        List<BookPost> bookPostListExpected = List.of();
        when(bookPostRepository.findAll()).thenReturn(bookPostListExpected);

        // Act
        List<BookPost> bookPostListActual = bookPostService.getAllBookPosts();

        // Assert
        assertEquals(bookPostListExpected, bookPostListActual);
    }

    @Test
    @DisplayName("getAllBookPosts() returns a list of book posts when the table is not empty")
    void getAllBookPostsWhenBookPostTableIsNotEmpty() {
        //Arrange
        User userOne = new User();
        userOne.setEmail("userOne@email");

        User userTwo = new User();
        userOne.setEmail("userTwo@email");

        BookPost bookOne = new BookPost(70L, "image_url_one", "Book Author 1", userOne,
                130L, "I hear this is a very good book", ReadingStatus.TBR, LocalDate.now());
        BookPost bookTwo = new BookPost(23L, "image_url_two", "Book Author 2", userTwo,
                338L, "I am just loving every bit of this amazing book",
                ReadingStatus.READING, LocalDate.now());

        List<BookPost> bookPostListExpected = new ArrayList<>();
        bookPostListExpected.add(bookOne);
        bookPostListExpected.add(bookTwo);

        when(bookPostRepository.findAll()).thenReturn(bookPostListExpected);

        // Act
        List<BookPost> bookPostListActual = bookPostService.getAllBookPosts();

        // Assert
        assertEquals(bookPostListExpected, bookPostListActual);
    }

    @Test
    void getBookPostById() {
    }

    @Test
    void getBookPostsByUser() {
    }
}