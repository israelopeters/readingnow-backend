package com.israelopeters.ReadingNow_Backend.service;

import com.israelopeters.ReadingNow_Backend.exception.BookPostNotFoundException;
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
import java.util.Optional;

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
    @DisplayName("getBookPostById() throws a BookNotFoundException when book post id does not exist in data store")
    void getBookPostByIdWhenBookPostDoesNotExist() {
        // Arrange
        when(bookPostRepository.findById(1L)).thenThrow(BookPostNotFoundException.class);

        // Act and assert
        assertThrows(BookPostNotFoundException.class, () -> bookPostService.getBookPostById(1L));
    }

    @Test
    @DisplayName("getBookPostById() returns a BookPost object when book exists in data store")
    void getBookPostByIdWhenBookPostExists() {
        // Arrange
        BookPost bookPostExpected = new BookPost(70L, "image_url_one", "Book Author 1", new User(),
                130L, "I hear this is a very good book", ReadingStatus.TBR, LocalDate.now());

        when(bookPostRepository.findById(1L)).thenReturn(Optional.of(bookPostExpected));

        // Act
        BookPost bookPostActual = bookPostService.getBookPostById(1L);

        // Assert
        assertEquals(bookPostExpected, bookPostActual);
    }

    @Test
    void getBookPostsByUser() {
    }
}