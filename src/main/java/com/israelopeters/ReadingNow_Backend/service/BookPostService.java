package com.israelopeters.ReadingNow_Backend.service;

import com.israelopeters.ReadingNow_Backend.model.BookPost;
import com.israelopeters.ReadingNow_Backend.model.User;
import com.israelopeters.ReadingNow_Backend.model.dto.BookPostCreationDto;

import java.util.List;

public interface BookPostService {
    List<BookPost> getAllBookPosts();
    BookPost getBookPostById(Long id);
    List<BookPost> getBookPostsByUser(String email);
    BookPost addBookPost(BookPostCreationDto bookPostCreationDto);
    User getAuthenticatedUser();
}
