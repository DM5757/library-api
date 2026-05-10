package com.davit.libraryapi.dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BookResponseDto {
    private Long id;
    private String title;
    private String isbn;
    private LocalDate publishedDate;
    private Long authorId;
    private String authorName;
}
