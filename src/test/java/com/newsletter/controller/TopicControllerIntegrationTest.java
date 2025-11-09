package com.newsletter.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.newsletter.dto.TopicDTO;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false) // Disable security for testing
@ActiveProfiles("test")
@Transactional
class TopicControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void createTopic_Success() throws Exception {
        TopicDTO topicDTO = new TopicDTO();
        topicDTO.setName("Integration Test Topic");
        topicDTO.setDescription("Test Description");

        mockMvc.perform(post("/api/topics")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(topicDTO)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data.name").value("Integration Test Topic"))
                .andExpect(jsonPath("$.data.description").value("Test Description"));
    }

    @Test
    void createTopic_InvalidData_ReturnsBadRequest() throws Exception {
        TopicDTO topicDTO = new TopicDTO();
        topicDTO.setName(""); // Empty name
        topicDTO.setDescription("Test Description");

        mockMvc.perform(post("/api/topics")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(topicDTO)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false));
    }

    @Test
    void getAllTopics_Success() throws Exception {
        mockMvc.perform(get("/api/topics"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.success").value(true))
                .andExpect(jsonPath("$.data").isArray());
    }

    @Test
    void getTopicById_NotFound() throws Exception {
        mockMvc.perform(get("/api/topics/99999"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.success").value(false))
                .andExpect(jsonPath("$.message").value("Topic not found with ID: 99999"));
    }
}
