package com.israelopeters.ReadingNow_Backend.service;

import com.israelopeters.ReadingNow_Backend.exception.BookPostNotFoundException;
import com.israelopeters.ReadingNow_Backend.exception.InvalidBookPostException;
import com.israelopeters.ReadingNow_Backend.exception.UserNotFoundException;
import com.israelopeters.ReadingNow_Backend.model.BookPost;
import com.israelopeters.ReadingNow_Backend.model.User;
import com.israelopeters.ReadingNow_Backend.model.dto.BookPostCreationDto;
import com.israelopeters.ReadingNow_Backend.model.dto.DtoMapper;
import com.israelopeters.ReadingNow_Backend.repository.BookPostRepository;
import com.israelopeters.ReadingNow_Backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookPostServiceImpl implements BookPostService {

    @Autowired
    BookPostRepository bookPostRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    DtoMapper mapper;

    @Override
    public List<BookPost> getAllBookPosts() {
        return new ArrayList<>(bookPostRepository.findAll());
    }

    @Override
    public BookPost getBookPostById(Long id) {
        Optional<BookPost> bookPost = bookPostRepository.findById(id);
        if (bookPost.isPresent()) {
            return bookPost.get();
        } else {
            throw new BookPostNotFoundException("Book does not exist!");
        }
    }

    @Override
    public List<BookPost> getBookPostsByUser(String email) {
        if (userRepository.findByEmail(email).isEmpty()) {
            throw new UserNotFoundException("User not found!");
        }
        return new ArrayList<>(bookPostRepository
                .findAll()
                .stream()
                .filter(bookPost -> email.equals(bookPost.getUserAuthor().getEmail()))
                .toList());
    }

    @Override
    public BookPost addBookPost(BookPostCreationDto bookPostCreationDto) {
        // Check whether the input object is valid
        try {
            if (!isValidBookCreationPost(bookPostCreationDto)) {
                throw new InvalidBookPostException("Invalid book post");
            }
        } catch (NullPointerException e) {
            // Exception handling for when a user sends an invalid (non-BookPostCreationDto) request body to the endpoint
            throw new NullPointerException("Invalid book post");
        }

        // Update the necessary fields
        BookPost newBookPost = mapper.toBookPost(bookPostCreationDto);
        newBookPost.setUserAuthor(getAuthenticatedUser());
        newBookPost.setDateCreated(LocalDate.now());

        return bookPostRepository.save(newBookPost);
    }

    private boolean isValidBookCreationPost(BookPostCreationDto bookPostCreationDto) {
        return !bookPostCreationDto.getBookSummary().isBlank()
                & !bookPostCreationDto.getBookAuthor().isBlank()
                & bookPostCreationDto.getReadingStatus() != null
                & bookPostCreationDto.getNumberOfPages() != null;
    }

    @Override
    public User getAuthenticatedUser() {
        SecurityContext context = SecurityContextHolder.getContext();
        Authentication authentication = context.getAuthentication();
        String username;
        Object principal = authentication.getPrincipal();
        if (principal instanceof UserDetails) {
            username = ((UserDetails) principal).getUsername();
        } else {
            username = principal.toString();
        }

        return userRepository.findByEmail(username).get();
    }
}
