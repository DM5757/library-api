package com.davit.libraryapi.repository;

import com.davit.libraryapi.entity.Author;
import com.davit.libraryapi.entity.Book;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class RepositoryTest {

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private BookRepository bookRepository;

    @Test
    void authorRepository_ExistsByEmail_Works() {
        Author author = Author.builder()
                .fullName("Test Author")
                .email("test@example.com")
                .build();
        authorRepository.save(author);

        assertTrue(authorRepository.existsByEmail("test@example.com"));
        assertFalse(authorRepository.existsByEmail("other@example.com"));
    }

    @Test
    void bookRepository_CanSaveAndFindBookWithAuthor() {
        Author author = Author.builder()
                .fullName("Test Author")
                .email("test@example.com")
                .build();
        Author savedAuthor = authorRepository.save(author);

        Book book = Book.builder()
                .title("Test Book")
                .isbn("123456789")
                .publishedDate(LocalDate.now())
                .author(savedAuthor)
                .build();
        Book savedBook = bookRepository.save(book);

        assertNotNull(savedBook.getId());
        assertEquals(savedAuthor.getId(), savedBook.getAuthor().getId());
        
        Book foundBook = bookRepository.findById(savedBook.getId()).orElse(null);
        assertNotNull(foundBook);
        assertEquals("Test Book", foundBook.getTitle());
    }
}
