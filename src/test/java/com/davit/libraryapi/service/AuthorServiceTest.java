package com.davit.libraryapi.service;

import com.davit.libraryapi.dto.AuthorRequestDto;
import com.davit.libraryapi.dto.AuthorResponseDto;
import com.davit.libraryapi.entity.Author;
import com.davit.libraryapi.exception.EmailAlreadyExistsException;
import com.davit.libraryapi.exception.ResourceNotFoundException;
import com.davit.libraryapi.repository.AuthorRepository;
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
class AuthorServiceTest {

    @Mock
    private AuthorRepository authorRepository;

    private MeterRegistry meterRegistry;

    private AuthorService authorService;

    @BeforeEach
    void setUp() {
        meterRegistry = new SimpleMeterRegistry();
        authorService = new AuthorService(authorRepository, meterRegistry);
    }

    @Test
    void createAuthor_Success() {
        AuthorRequestDto request = AuthorRequestDto.builder()
                .fullName("John Doe")
                .email("john@example.com")
                .build();

        Author author = Author.builder().id(1L).fullName("John Doe").email("john@example.com").build();

        when(authorRepository.existsByEmail(request.getEmail())).thenReturn(false);
        when(authorRepository.save(any(Author.class))).thenReturn(author);

        AuthorResponseDto result = authorService.createAuthor(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getFullName());
        verify(authorRepository).save(any(Author.class));
        assertEquals(1, meterRegistry.get("library.authors.created").counter().count());
    }

    @Test
    void createAuthor_DuplicateEmail_ThrowsException() {
        AuthorRequestDto request = AuthorRequestDto.builder()
                .fullName("John Doe")
                .email("duplicate@example.com")
                .build();

        when(authorRepository.existsByEmail(request.getEmail())).thenReturn(true);

        assertThrows(EmailAlreadyExistsException.class, () -> authorService.createAuthor(request));
        verify(authorRepository, never()).save(any(Author.class));
    }

    @Test
    void getAuthorById_NotFound_ThrowsException() {
        when(authorRepository.findById(1L)).thenReturn(Optional.empty());

        assertThrows(ResourceNotFoundException.class, () -> authorService.getAuthorById(1L));
    }
}
