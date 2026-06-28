package com.davit.libraryapi.service;

import com.davit.libraryapi.dto.BookRequestDto;
import com.davit.libraryapi.dto.BookResponseDto;
import com.davit.libraryapi.entity.Author;
import com.davit.libraryapi.entity.Book;
import com.davit.libraryapi.exception.ResourceNotFoundException;
import com.davit.libraryapi.repository.AuthorRepository;
import com.davit.libraryapi.repository.BookRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class BookServiceTest {

    @Mock
    private BookRepository bookRepository;

    @Mock
    private AuthorRepository authorRepository;

    private MeterRegistry meterRegistry;

    private BookService bookService;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        bookService = new BookService(bookRepository, authorRepository, meterRegistry);
    }

    @Test
    void createBook_Success_WhenAuthorExists() {
        Author author = Author.builder().id(1L).fullName("Author Name").build();
        BookRequestDto request = BookRequestDto.builder()
                .title("Book Title")
                .authorId(1L)
                .build();
        Book book = Book.builder().id(1L).title("Book Title").author(author).build();

        when(authorRepository.findById(1L)).thenReturn(Optional.of(author));
        when(bookRepository.save(any(Book.class))).thenReturn(book);

        BookResponseDto result = bookService.createBook(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        verify(bookRepository).save(any(Book.class));
        assertEquals(1, meterRegistry.get("library.books.created").counter().count());
    }

    @Test
    void createBook_Fails_WhenAuthorDoesNotExist() {
        BookRequestDto request = BookRequestDto.builder()
                .title("Book Title")
                .authorId(1L)
                .build();

        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> bookService.createBook(request));
        verify(bookRepository, never()).save(any(Book.class));
    }

    @Test
    void deleteBook_NotFound_ThrowsException() {
        when(bookRepository.existsById(1L)).thenReturn(false);

        assertThrows(ResourceNotFoundException.class, () -> bookService.deleteBook(1L));
    }
}
