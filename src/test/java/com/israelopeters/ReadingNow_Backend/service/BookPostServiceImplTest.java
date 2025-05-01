package com.israelopeters.ReadingNow_Backend.service;

import com.israelopeters.ReadingNow_Backend.exception.BookPostNotFoundException;
import com.israelopeters.ReadingNow_Backend.exception.InvalidBookPostException;
import com.israelopeters.ReadingNow_Backend.exception.UserNotFoundException;
import com.israelopeters.ReadingNow_Backend.model.BookPost;
import com.israelopeters.ReadingNow_Backend.model.ReadingStatus;
import com.israelopeters.ReadingNow_Backend.model.User;
import com.israelopeters.ReadingNow_Backend.model.dto.BookPostCreationDto;
import com.israelopeters.ReadingNow_Backend.model.dto.DtoMapper;
import com.israelopeters.ReadingNow_Backend.repository.BookPostRepository;
import com.israelopeters.ReadingNow_Backend.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.security.core.Authentication;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.when;

@DataJpaTest
class BookPostServiceImplTest {

    @Mock
    BookPostRepository bookPostRepository;

    @Mock
    UserRepository userRepository;

    @Mock
    DtoMapper mapper;

    @Mock
    Authentication authentication;

    @InjectMocks
    BookPostServiceImpl bookPostService;

    @Test
    @DisplayName("getAllBookPosts() returns an empty list when book post table is empty")
    void getAllBookPosts_whenBookPostTableIsEmpty_returnsAnEmptyList() {
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
    void getAllBookPosts_whenBookPostTableIsNotEmpty_returnsAListOfAvailableBooks() {
        //Arrange
        User userOne = new User();
        userOne.setEmail("userOne@email.com");

        User userTwo = new User();
        userOne.setEmail("userTwo@email.com");

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
    void getBookPostById_whenBookPostDoesNotExist_throwsBookPostNotFoundException() {
        // Arrange
        when(bookPostRepository.findById(1L)).thenThrow(BookPostNotFoundException.class);

        // Act and assert
        assertThrows(BookPostNotFoundException.class, () -> bookPostService.getBookPostById(1L));
    }

    @Test
    @DisplayName("getBookPostById() returns a BookPost object when book exists in data store")
    void getBookPostById_whenBookPostIdExists_returnsBookPostObject() {
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
    @DisplayName("getBookPostsByUser() throws a UserNotFoundException when user does not exist")
    void getBookPostsByUser_whenUserDoesNotExist_throwsUserNotFoundException() {
        //Arrange
        when(userRepository.findByEmail("userone@email.com")).thenReturn(Optional.empty());

        // Act and assert
        assertThrows(UserNotFoundException.class, () -> bookPostService.getBookPostsByUser("userone@email.com"));
    }

    @Test
    @DisplayName("getBookPostsByUser() returns a list of book posts belonging to given user when the user exists")
    void getBookPostsByUser_whenUserExists_returnsListOfBookPostsByUser() {
        // Arrange
        User userOne = new User();
        userOne.setEmail("userone@email.com");

        User userTwo = new User();
        userTwo.setEmail("usertwo@email.com");

        User userThree = new User();
        userThree.setEmail("userthree@email.com");

        BookPost bookOne = new BookPost(70L, "image_url_one", "Book Author 1", userOne,
                130L, "I hear this is a very good book", ReadingStatus.TBR, LocalDate.now());
        BookPost bookTwo = new BookPost(23L, "image_url_two", "Book Author 2", userTwo,
                338L, "I am just loving every bit of this amazing book",
                ReadingStatus.READING, LocalDate.now());
        BookPost bookThree = new BookPost(43L, "image_url_three", "Book Author 3", userOne,
                105L, "Enjoyed this one!",
                ReadingStatus.COMPLETED, LocalDate.now());
        BookPost bookFour = new BookPost(55L, "image_url_four", "Book Author 4", userThree,
                220L, "I can't seem to be getting enough of this.",
                ReadingStatus.READING, LocalDate.now());

        List<BookPost> bookPostListAll = new ArrayList<>(List.of(bookOne, bookTwo, bookThree, bookFour));
        List<BookPost> bookPostListExpected = new ArrayList<>(List.of(bookOne, bookThree));

        when(bookPostRepository.findAll()).thenReturn(bookPostListAll);
        when(userRepository.findByEmail("userone@email.com")).thenReturn(Optional.of(userOne));

        // Act
        List<BookPost> bookPostListActual = bookPostService.getBookPostsByUser("userone@email.com");

        // Assert
        assertEquals(bookPostListExpected, bookPostListActual);
    }

    @Test
    @DisplayName("addBookPost() throws an InvalidBookPostException when book summary field is blank")
    void addBookPost_whenBookSummaryFieldIsBlank_throwsInvalidBookPostException() {
        // Arrange
        BookPostCreationDto bookPostCreationDto = new BookPostCreationDto("image_url_one",
                "Book Author 1",130L, " ",
                ReadingStatus.TBR);

        // Act and assert
        assertThrows(InvalidBookPostException.class, () -> bookPostService.addBookPost(bookPostCreationDto));
    }

    @Test
    @DisplayName("addBookPost() throws an InvalidBookPostException when book author field is blank")
    void addBookPost_whenBookAuthorFieldIsBlank_throwsInvalidBookPostException() {
        // Arrange
        BookPostCreationDto bookPostCreationDto = new BookPostCreationDto("image_url_one",
                "",130L, "I hear this is a very good book",
                ReadingStatus.TBR);

        // Act and assert
        assertThrows(InvalidBookPostException.class, () -> bookPostService.addBookPost(bookPostCreationDto));
    }

    @Test
    @DisplayName("addBookPost() throws an InvalidBookPostException when number of pages field is null")
    void addBookPost_whenNumberOfPagesFieldIsNull_throwsInvalidBookPostException() {
        // Arrange
        BookPostCreationDto bookPostCreationDto = new BookPostCreationDto("image_url_one", " ",
                null, "I hear this is a very good book", ReadingStatus.TBR);

        // Act and assert
        assertThrows(InvalidBookPostException.class, () -> bookPostService.addBookPost(bookPostCreationDto));
    }
    @Test
    @DisplayName("addBookPost() throws an InvalidBookPostException when reading status field is null")
    void addBookPost_whenReadingStatusFieldIsNull_throwsInvalidBookPostException() {
        // Arrange
        BookPostCreationDto bookPostCreationDto = new BookPostCreationDto("image_url_one",
                "Book Author 1",130L, "I hear this is a very good book",
                null);

        // Act and assert
        assertThrows(InvalidBookPostException.class, () -> bookPostService.addBookPost(bookPostCreationDto));
    }

    // TODO: Write unit test for "addBookPost() returns BookPost object when input book post is valid"
//    @Test
//    @DisplayName("addBookPost() returns BookPost object when input book post is valid")
//    void addBookPost_whenInputBookPostIsValid_returnsBookPostOfPersistedBook() {
//        // Arrange
//        User user = new User();
//        user.setEmail("userOne@email.com");
//        BookPostCreationDto bookPostCreationDto = new BookPostCreationDto("image_url",
//                "Book Author",130L, "I hear this is a very good book",
//                ReadingStatus.TBR);
//        BookPost bookPostExpected = new BookPost(1L, "image_url","Book Author", user,
//                130L, "I hear this is a very good book", ReadingStatus.TBR, LocalDate.now());
//
//        when(mapper.toBookPost(bookPostCreationDto)).thenReturn(bookPostExpected);
//        when(bookPostService.getAuthenticatedUser()).thenReturn(user); // Throwing null pointer exception for Authentication
//        when(bookPostRepository.save(bookPostExpected)).thenReturn(bookPostExpected);
//
//        // Act
//        BookPost bookPostActual = bookPostService.addBookPost(bookPostCreationDto);
//
//        // Assert
//        assertEquals(bookPostExpected, bookPostActual);
//    }
}