package com.davit.libraryapi.controller;

import com.davit.libraryapi.dto.AuthorRequestDto;
import com.davit.libraryapi.service.AuthorService;
import com.davit.libraryapi.service.BookService;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ControllerSecurityTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private AuthorService authorService;

    @MockBean
    private BookService bookService;

    @TestConfiguration
    static class TestMetricsConfig {
        @Bean
        MeterRegistry meterRegistry() {
            return new SimpleMeterRegistry();
        }
    }

    @Test
    void anonymous_AccessToProtectedApi_Returns401() throws Exception {
        mockMvc.perform(get("/api/authors"))
                .andExpect(status().isUnauthorized());
    }

    @Test
    @WithMockUser(roles = "USER")
    void user_CanUseGetEndpoints() throws Exception {
        mockMvc.perform(get("/api/authors"))
                .andExpect(status().isOk());
        mockMvc.perform(get("/api/books"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(roles = "USER")
    void user_CannotUsePost_Returns403() throws Exception {
        AuthorRequestDto request = AuthorRequestDto.builder()
                .fullName("Test")
                .email("test@example.com")
                .build();

        mockMvc.perform(post("/api/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void admin_CanCreateAuthor_Returns201() throws Exception {
        AuthorRequestDto request = AuthorRequestDto.builder()
                .fullName("Admin Author")
                .email("admin@example.com")
                .build();

        mockMvc.perform(post("/api/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void admin_InvalidData_Returns400() throws Exception {
        AuthorRequestDto request = AuthorRequestDto.builder()
                .fullName("") // Invalid
                .email("invalid-email") // Invalid
                .build();

        mockMvc.perform(post("/api/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }
}
