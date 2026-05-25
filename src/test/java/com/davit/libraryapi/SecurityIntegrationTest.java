package com.davit.libraryapi;

import com.davit.libraryapi.dto.AuthorRequestDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class SecurityIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void anonymousUserCannotAccessProtectedApi() throws Exception {
        mockMvc.perform(get("/api/authors"))
                .andExpect(status().isUnauthorized()); // Returns 401 instead of redirect
    }

    @Test
    void rootRedirectsToSwagger() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().is3xxRedirection())
                .andExpect(header().string("Location", "/swagger-ui/index.html"));
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void userCanAccessGetAuthors() throws Exception {
        mockMvc.perform(get("/api/authors"))
                .andExpect(status().isOk());
    }

    @Test
    @WithMockUser(username = "user", roles = "USER")
    void userCannotPostAuthor() throws Exception {
        AuthorRequestDto author = AuthorRequestDto.builder()
                .fullName("Secured Author")
                .email("secured@example.com")
                .build();

        mockMvc.perform(post("/api/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(author)))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(username = "admin", roles = "ADMIN")
    void adminCanPostAuthor() throws Exception {
        AuthorRequestDto author = AuthorRequestDto.builder()
                .fullName("Admin Author")
                .email("admin_created@example.com")
                .build();

        mockMvc.perform(post("/api/authors")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(author)))
                .andExpect(status().isCreated());
    }
}
