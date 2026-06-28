package com.davit.libraryapi.service;

import com.davit.libraryapi.dto.AuthorRequestDto;
import com.davit.libraryapi.dto.AuthorResponseDto;
import com.davit.libraryapi.entity.Author;
import com.davit.libraryapi.exception.EmailAlreadyExistsException;
import com.davit.libraryapi.exception.ResourceNotFoundException;
import com.davit.libraryapi.repository.AuthorRepository;
import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.MeterRegistry;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class AuthorService {

    private final AuthorRepository authorRepository;
    private final Counter authorCreatedCounter;

    public AuthorService(AuthorRepository authorRepository, MeterRegistry meterRegistry) {
        this.authorRepository = authorRepository;
        this.authorCreatedCounter = Counter.builder("library.authors.created")
                .description("Total number of authors created")
                .register(meterRegistry);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public AuthorResponseDto createAuthor(AuthorRequestDto requestDto) {
        if (authorRepository.existsByEmail(requestDto.getEmail())) {
            log.warn("Attempt to create author with duplicate email: {}", requestDto.getEmail());
            throw new EmailAlreadyExistsException("error.email.exists");
        }
        Author author = Author.builder()
                .fullName(requestDto.getFullName())
                .email(requestDto.getEmail())
                .build();
        Author savedAuthor = authorRepository.save(author);
        log.info("Author created with id: {}", savedAuthor.getId());
        authorCreatedCounter.increment();
        return mapToResponseDto(savedAuthor);
    }

    public List<AuthorResponseDto> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public AuthorResponseDto getAuthorById(Long id) {
        log.debug("Fetching author with id: {}", id);
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> {
                    log.warn("Author not found with id: {}", id);
                    return new ResourceNotFoundException("error.author.notFound", id);
                });
        return mapToResponseDto(author);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public AuthorResponseDto updateAuthor(Long id, AuthorRequestDto requestDto) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("error.author.notFound", id));
        
        authorRepository.findByEmail(requestDto.getEmail())
                .ifPresent(existingAuthor -> {
                    if (!existingAuthor.getId().equals(id)) {
                        throw new EmailAlreadyExistsException("error.email.exists");
                    }
                });

        author.setFullName(requestDto.getFullName());
        author.setEmail(requestDto.getEmail());
        
        Author updatedAuthor = authorRepository.save(author);
        return mapToResponseDto(updatedAuthor);
    }

    @Transactional
    @PreAuthorize("hasRole('ADMIN')")
    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new ResourceNotFoundException("error.author.notFound", id);
        }
        authorRepository.deleteById(id);
    }

    private AuthorResponseDto mapToResponseDto(Author author) {
        return AuthorResponseDto.builder()
                .id(author.getId())
                .fullName(author.getFullName())
                .email(author.getEmail())
                .build();
    }
}
