package com.newsletter.controller;

import com.newsletter.dto.ApiResponse;
import com.newsletter.dto.SubscriberDTO;
import com.newsletter.service.SubscriberService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/subscribers")
@RequiredArgsConstructor
public class SubscriberController {

    private final SubscriberService subscriberService;

    @PostMapping
    public ResponseEntity<ApiResponse<SubscriberDTO>> createSubscriber(
            @Valid @RequestBody SubscriberDTO subscriberDTO) {
        SubscriberDTO createdSubscriber = subscriberService.createSubscriber(subscriberDTO);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("Subscriber created successfully", createdSubscriber));
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<SubscriberDTO>>> getAllSubscribers() {
        List<SubscriberDTO> subscribers = subscriberService.getAllSubscribers();
        return ResponseEntity.ok(ApiResponse.success("Subscribers fetched successfully", subscribers));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<SubscriberDTO>> getSubscriberById(@PathVariable Long id) {
        SubscriberDTO subscriber = subscriberService.getSubscriberById(id);
        return ResponseEntity.ok(ApiResponse.success("Subscriber fetched successfully", subscriber));
    }

    @GetMapping("/topic/{topicId}")
    public ResponseEntity<ApiResponse<List<SubscriberDTO>>> getSubscribersByTopic(@PathVariable Long topicId) {
        List<SubscriberDTO> subscribers = subscriberService.getSubscribersByTopicId(topicId);
        return ResponseEntity.ok(ApiResponse.success("Subscribers fetched successfully", subscribers));
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<SubscriberDTO>> updateSubscriber(
            @PathVariable Long id,
            @Valid @RequestBody SubscriberDTO subscriberDTO) {
        SubscriberDTO updatedSubscriber = subscriberService.updateSubscriber(id, subscriberDTO);
        return ResponseEntity.ok(ApiResponse.success("Subscriber updated successfully", updatedSubscriber));
    }

    @PatchMapping("/{id}/unsubscribe")
    public ResponseEntity<ApiResponse<Void>> unsubscribe(@PathVariable Long id) {
        subscriberService.unsubscribe(id);
        return ResponseEntity.ok(ApiResponse.success("Unsubscribed successfully", null));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteSubscriber(@PathVariable Long id) {
        subscriberService.deleteSubscriber(id);
        return ResponseEntity.ok(ApiResponse.success("Subscriber deleted successfully", null));
    }
}
