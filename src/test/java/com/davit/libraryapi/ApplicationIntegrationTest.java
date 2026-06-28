package com.davit.libraryapi;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ApplicationIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void metadataEndpoint_ReturnsCustomAppSettings() throws Exception {
        mockMvc.perform(get("/api/metadata"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.applicationTitle").value("Library API Test"))
                .andExpect(jsonPath("$.supportEmail").value("test@example.com"));
    }

    @Test
    void actuatorHealth_IsPubliclyAvailable() throws Exception {
        mockMvc.perform(get("/actuator/health"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("UP"))
                .andExpect(jsonPath("$.components.library.details.message").value("Library API is running"));
    }

    @Test
    void actuatorInfo_IsPubliclyAvailable() throws Exception {
        mockMvc.perform(get("/actuator/info"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.app.name").value("Library API"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void actuatorMetrics_IsAvailableForAdmin() throws Exception {
        mockMvc.perform(get("/actuator/metrics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.names").isArray());
    }

    @Test
    @WithMockUser(roles = "USER")
    void actuatorMetrics_IsForbiddenForUser() throws Exception {
        mockMvc.perform(get("/actuator/metrics"))
                .andExpect(status().isForbidden());
    }
}
