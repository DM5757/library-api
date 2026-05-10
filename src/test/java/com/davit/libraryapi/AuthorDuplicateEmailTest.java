package com.davit.libraryapi;

import com.davit.libraryapi.dto.AuthorRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthorDuplicateEmailTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testCreateDuplicateAuthorEmail() throws Exception {
        AuthorRequestDto author1 = AuthorRequestDto.builder()
                .fullName("Author One")
                .email("duplicate@example.com")
                .build();

        mockMvc.perform(post("/api/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(author1)))
                .andExpect(status().isCreated());

        AuthorRequestDto author2 = AuthorRequestDto.builder()
                .fullName("Author Two")
                .email("duplicate@example.com")
                .build();

        // This should return 409 Conflict after the fix
        mockMvc.perform(post("/api/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(author2)))
                .andExpect(status().isConflict());
    }
}
