package com.davit.libraryapi.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuthorResponseDto {
    private Long id;
    private String fullName;
    private String email;
}
