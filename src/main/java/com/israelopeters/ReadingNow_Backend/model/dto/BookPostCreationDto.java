package com.israelopeters.ReadingNow_Backend.model.dto;

import com.israelopeters.ReadingNow_Backend.model.ReadingStatus;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class BookPostCreationDto {

    private String imageUrl;

    private String bookAuthor;

    private Long numberOfPages;

    private String bookSummary;

    private ReadingStatus readingStatus;
}
