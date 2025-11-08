package com.newsletter.controller;

import com.newsletter.dto.ApiResponse;
import com.newsletter.dto.TopicDTO;
import com.newsletter.service.TopicService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/topics")
@RequiredArgsConstructor
public class TopicController {

    private final TopicService topicService;

    @PostMapping
    public ResponseEntity<ApiResponse<TopicDTO>> createTopic(@Valid @RequestBody TopicDTO topicDTO) {
        TopicDTO createdTopic = topicService.createTopic(topicDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Topic created successfully", createdTopic));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<TopicDTO>>> getAllTopics() {
        List<TopicDTO> topics = topicService.getAllTopics();
        return ResponseEntity.ok(ApiResponse.success("Topics fetched successfully", topics));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<TopicDTO>> getTopicById(@PathVariable Long id) {
        TopicDTO topic = topicService.getTopicById(id);
        return ResponseEntity.ok(ApiResponse.success("Topic fetched successfully", topic));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<TopicDTO>> updateTopic(
            @PathVariable Long id,
            @Valid @RequestBody TopicDTO topicDTO) {
        TopicDTO updatedTopic = topicService.updateTopic(id, topicDTO);
        return ResponseEntity.ok(ApiResponse.success("Topic updated successfully", updatedTopic));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteTopic(@PathVariable Long id) {
        topicService.deleteTopic(id);
        return ResponseEntity.ok(ApiResponse.success("Topic deleted successfully", null));
    }
}
