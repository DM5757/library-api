package com.davit.libraryapi.controller;

import com.davit.libraryapi.dto.AuthorRequestDto;
import com.davit.libraryapi.dto.AuthorResponseDto;
import com.davit.libraryapi.service.AuthorService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/authors")
@RequiredArgsConstructor
@Tag(name = "Authors", description = "Management APIs for authors")
public class AuthorController {

    private final AuthorService authorService;

    @PostMapping
    @Operation(summary = "Create a new author", description = "Add a new author to the library")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Created"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "409", description = "Conflict - Duplicate Email")
    })
    public ResponseEntity<AuthorResponseDto> createAuthor(@Valid @RequestBody AuthorRequestDto requestDto) {
        return new ResponseEntity<>(authorService.createAuthor(requestDto), HttpStatus.CREATED);
    }

    @GetMapping
    @Operation(summary = "Get all authors", description = "Retrieve a list of all authors")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized")
    })
    public ResponseEntity<List<AuthorResponseDto>> getAllAuthors() {
        return ResponseEntity.ok(authorService.getAllAuthors());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get author by ID", description = "Retrieve a specific author using their unique ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<AuthorResponseDto> getAuthorById(@PathVariable Long id) {
        return ResponseEntity.ok(authorService.getAuthorById(id));
    }

    @PutMapping("/{id}")
    @Operation(summary = "Update an author", description = "Modify an existing author's information")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "OK"),
            @ApiResponse(responseCode = "400", description = "Bad Request"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found"),
            @ApiResponse(responseCode = "409", description = "Conflict - Duplicate Email")
    })
    public ResponseEntity<AuthorResponseDto> updateAuthor(@PathVariable Long id, @Valid @RequestBody AuthorRequestDto requestDto) {
        return ResponseEntity.ok(authorService.updateAuthor(id, requestDto));
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Delete an author", description = "Remove an author from the library")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "No Content"),
            @ApiResponse(responseCode = "401", description = "Unauthorized"),
            @ApiResponse(responseCode = "403", description = "Forbidden"),
            @ApiResponse(responseCode = "404", description = "Not Found")
    })
    public ResponseEntity<Void> deleteAuthor(@PathVariable Long id) {
        authorService.deleteAuthor(id);
        return ResponseEntity.noContent().build();
    }
}
