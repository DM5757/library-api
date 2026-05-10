package com.davit.libraryapi.service;

import com.davit.libraryapi.dto.AuthorRequestDto;
import com.davit.libraryapi.dto.AuthorResponseDto;
import com.davit.libraryapi.entity.Author;
import com.davit.libraryapi.exception.EmailAlreadyExistsException;
import com.davit.libraryapi.exception.ResourceNotFoundException;
import com.davit.libraryapi.repository.AuthorRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthorService {

    private final AuthorRepository authorRepository;

    @Transactional
    public AuthorResponseDto createAuthor(AuthorRequestDto requestDto) {
        if (authorRepository.existsByEmail(requestDto.getEmail())) {
            throw new EmailAlreadyExistsException("Author with this email already exists");
        }
        Author author = Author.builder()
                .fullName(requestDto.getFullName())
                .email(requestDto.getEmail())
                .build();
        Author savedAuthor = authorRepository.save(author);
        return mapToResponseDto(savedAuthor);
    }

    public List<AuthorResponseDto> getAllAuthors() {
        return authorRepository.findAll().stream()
                .map(this::mapToResponseDto)
                .collect(Collectors.toList());
    }

    public AuthorResponseDto getAuthorById(Long id) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
        return mapToResponseDto(author);
    }

    @Transactional
    public AuthorResponseDto updateAuthor(Long id, AuthorRequestDto requestDto) {
        Author author = authorRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Author not found with id: " + id));
        
        authorRepository.findByEmail(requestDto.getEmail())
                .ifPresent(existingAuthor -> {
                    if (!existingAuthor.getId().equals(id)) {
                        throw new EmailAlreadyExistsException("Author with this email already exists");
                    }
                });

        author.setFullName(requestDto.getFullName());
        author.setEmail(requestDto.getEmail());
        
        Author updatedAuthor = authorRepository.save(author);
        return mapToResponseDto(updatedAuthor);
    }

    @Transactional
    public void deleteAuthor(Long id) {
        if (!authorRepository.existsById(id)) {
            throw new ResourceNotFoundException("Author not found with id: " + id);
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
