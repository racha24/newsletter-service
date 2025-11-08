package com.newsletter.controller;

import com.newsletter.dto.ApiResponse;
import com.newsletter.dto.ContentDTO;
import com.newsletter.model.Content.ContentStatus;
import com.newsletter.service.ContentService;
import com.newsletter.service.NewsletterSchedulerService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/content")
@RequiredArgsConstructor
public class ContentController {

    private final ContentService contentService;
    private final NewsletterSchedulerService schedulerService;

    @PostMapping
    public ResponseEntity<ApiResponse<ContentDTO>> createContent(@Valid @RequestBody ContentDTO contentDTO) {
        ContentDTO createdContent = contentService.createContent(contentDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Content created successfully", createdContent));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<ContentDTO>>> getAllContent() {
        List<ContentDTO> contents = contentService.getAllContent();
        return ResponseEntity.ok(ApiResponse.success("Content fetched successfully", contents));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<ContentDTO>> getContentById(@PathVariable Long id) {
        ContentDTO content = contentService.getContentById(id);
        return ResponseEntity.ok(ApiResponse.success("Content fetched successfully", content));
    }

    @GetMapping("/topic/{topicId}")
    public ResponseEntity<ApiResponse<List<ContentDTO>>> getContentByTopic(@PathVariable Long topicId) {
        List<ContentDTO> contents = contentService.getContentByTopicId(topicId);
        return ResponseEntity.ok(ApiResponse.success("Content fetched successfully", contents));
    }

    @GetMapping("/status/{status}")
    public ResponseEntity<ApiResponse<List<ContentDTO>>> getContentByStatus(@PathVariable ContentStatus status) {
        List<ContentDTO> contents = contentService.getContentByStatus(status);
        return ResponseEntity.ok(ApiResponse.success("Content fetched successfully", contents));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<ContentDTO>> updateContent(
            @PathVariable Long id,
            @Valid @RequestBody ContentDTO contentDTO) {
        ContentDTO updatedContent = contentService.updateContent(id, contentDTO);
        return ResponseEntity.ok(ApiResponse.success("Content updated successfully", updatedContent));
    }

    @PatchMapping("/{id}/cancel")
    public ResponseEntity<ApiResponse<Void>> cancelContent(@PathVariable Long id) {
        contentService.cancelContent(id);
        return ResponseEntity.ok(ApiResponse.success("Content cancelled successfully", null));
    }

    @PostMapping("/{id}/send-now")
    public ResponseEntity<ApiResponse<Void>> sendNow(@PathVariable Long id) {
        schedulerService.sendNewsletterNow(id);
        return ResponseEntity.ok(ApiResponse.success("Newsletter sent successfully", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteContent(@PathVariable Long id) {
        contentService.deleteContent(id);
        return ResponseEntity.ok(ApiResponse.success("Content deleted successfully", null));
    }
}
