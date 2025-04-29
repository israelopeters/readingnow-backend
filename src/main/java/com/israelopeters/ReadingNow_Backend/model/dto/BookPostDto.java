package com.israelopeters.ReadingNow_Backend.model.dto;

import com.israelopeters.ReadingNow_Backend.model.ReadingStatus;
import com.israelopeters.ReadingNow_Backend.model.User;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookPostDto {

    private String imageUrl;

    private String bookAuthor;

    private User userAuthor;

    private Long numberOfPages;

    private String bookSummary;

    private ReadingStatus readingStatus;

    private LocalDate dateCreated;
}
