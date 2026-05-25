package com.davit.libraryapi.service;

import com.davit.libraryapi.dto.BookRequestDto;
import com.davit.libraryapi.dto.BookResponseDto;
import com.davit.libraryapi.entity.Author;
import com.davit.libraryapi.entity.Book;
import com.davit.libraryapi.exception.ResourceNotFoundException;
import com.davit.libraryapi.repository.AuthorRepository;
import com.davit.libraryapi.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final AuthorRepository authorRepository;

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public BookResponseDto createBook(BookRequestDto requestDto) {
        Author author = authorRepository.findById(requestDto.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + requestDto.getAuthorId()));

        Book book = Book.builder()
                .title(requestDto.getTitle())
                .isbn(requestDto.getIsbn())
                .publishedDate(requestDto.getPublishedDate())
                .author(author)
                .build();
        
        Book savedBook = bookRepository.save(book);
        return mapToResponseDto(savedBook);
    }

    public List<BookResponseDto> getAllBooks() {
        return bookRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public BookResponseDto getBookById(Long id) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        return mapToResponseDto(book);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public BookResponseDto updateBook(Long id, BookRequestDto requestDto) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Book not found with id: " + id));
        
        Author author = authorRepository.findById(requestDto.getAuthorId())
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + requestDto.getAuthorId()));

        book.setTitle(requestDto.getTitle());
        book.setIsbn(requestDto.getIsbn());
        book.setPublishedDate(requestDto.getPublishedDate());
        book.setAuthor(author);
        
        Book updatedBook = bookRepository.save(book);
        return mapToResponseDto(updatedBook);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteBook(Long id) {
        if (!bookRepository.existsById(id)) {
            throw new ResourceNotFoundException("Book not found with id: " + id);
        }
        bookRepository.deleteById(id);
    }

    private BookResponseDto mapToResponseDto(Book book) {
        return BookResponseDto.builder()
                .id(book.getId())
                .title(book.getTitle())
                .isbn(book.getIsbn())
                .publishedDate(book.getPublishedDate())
                .authorId(book.getAuthor().getId())
                .authorName(book.getAuthor().getFullName())
                .build();
    }
}
