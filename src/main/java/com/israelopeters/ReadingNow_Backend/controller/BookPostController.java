package com.israelopeters.ReadingNow_Backend.controller;

import com.israelopeters.ReadingNow_Backend.model.BookPost;
import com.israelopeters.ReadingNow_Backend.service.BookPostService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/bookposts")
public class BookPostController {

    @Autowired
    BookPostService bookPostService;

    @Tag(name = "get", description = "All GET methods")
    @Operation(summary = "Get all book posts", description = "Get all persisted book posts")
    @ApiResponse(responseCode = "200", description = "All book posts found",
            content = @Content(array = @ArraySchema(schema = @Schema(implementation = BookPost.class))))
    @GetMapping("/")
    public ResponseEntity<List<BookPost>> getAllBookPosts() {
        return new ResponseEntity<>(bookPostService.getAllBookPosts(), HttpStatus.OK);
    }
}
