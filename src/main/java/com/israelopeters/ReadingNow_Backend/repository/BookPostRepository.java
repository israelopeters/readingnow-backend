package com.israelopeters.ReadingNow_Backend.repository;

import com.israelopeters.ReadingNow_Backend.model.BookPost;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BookPostRepository extends JpaRepository<BookPost, Long> {
}
