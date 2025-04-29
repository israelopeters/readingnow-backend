package com.israelopeters.ReadingNow_Backend.service;

import com.israelopeters.ReadingNow_Backend.exception.BookPostNotFoundException;
import com.israelopeters.ReadingNow_Backend.exception.UserNotFoundException;
import com.israelopeters.ReadingNow_Backend.model.BookPost;
import com.israelopeters.ReadingNow_Backend.model.User;
import com.israelopeters.ReadingNow_Backend.repository.BookPostRepository;
import com.israelopeters.ReadingNow_Backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class BookPostServiceImpl implements BookPostService {

    @Autowired
    BookPostRepository bookPostRepository;

    @Autowired
    UserRepository userRepository;

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
}
