package com.israelopeters.ReadingNow_Backend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.time.LocalDate;

@Entity(name = "bookPost")
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookPost implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String imageUrl;

    private String bookAuthor;

    private User userAuthor;

    private Long numberOfPages;

    private String bookSummary;

    private ReadingStatus readingStatus;

    private LocalDate dateCreated;
}
