package com.newsletter.service;

import com.newsletter.dto.ContentDTO;
import com.newsletter.model.Content;
import com.newsletter.model.Content.ContentStatus;
import com.newsletter.model.Topic;
import com.newsletter.repository.ContentRepository;
import com.newsletter.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class ContentService {

    private final ContentRepository contentRepository;
    private final TopicRepository topicRepository;

    @Transactional
    public ContentDTO createContent(ContentDTO contentDTO) {
        log.info("Creating content for topic ID: {}", contentDTO.getTopicId());

        Topic topic = topicRepository.findById(contentDTO.getTopicId())
                .orElseThrow(() -> new IllegalArgumentException("Topic not found with ID: " + contentDTO.getTopicId()));

        if (contentDTO.getScheduledTime().isBefore(LocalDateTime.now())) {
            throw new IllegalArgumentException("Scheduled time must be in the future");
        }

        Content content = new Content();
        content.setSubject(contentDTO.getSubject());
        content.setBody(contentDTO.getBody());
        content.setTopic(topic);
        content.setScheduledTime(contentDTO.getScheduledTime());
        content.setStatus(ContentStatus.SCHEDULED);

        Content savedContent = contentRepository.save(content);
        log.info("Content created successfully with ID: {}", savedContent.getId());

        return convertToDTO(savedContent);
    }

    @Transactional(readOnly = true)
    public List<ContentDTO> getAllContent() {
        log.info("Fetching all content");
        return contentRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public ContentDTO getContentById(Long id) {
        log.info("Fetching content by ID: {}", id);
        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Content not found with ID: " + id));
        return convertToDTO(content);
    }

    @Transactional(readOnly = true)
    public List<ContentDTO> getContentByTopicId(Long topicId) {
        log.info("Fetching content for topic ID: {}", topicId);
        return contentRepository.findByTopicId(topicId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ContentDTO> getContentByStatus(ContentStatus status) {
        log.info("Fetching content by status: {}", status);
        return contentRepository.findByStatus(status).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public ContentDTO updateContent(Long id, ContentDTO contentDTO) {
        log.info("Updating content ID: {}", id);

        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Content not found with ID: " + id));

        if (content.getStatus() == ContentStatus.SENT) {
            throw new IllegalArgumentException("Cannot update content that has already been sent");
        }

        content.setSubject(contentDTO.getSubject());
        content.setBody(contentDTO.getBody());

        if (contentDTO.getScheduledTime() != null &&
                contentDTO.getScheduledTime().isAfter(LocalDateTime.now())) {
            content.setScheduledTime(contentDTO.getScheduledTime());
        }

        Content updatedContent = contentRepository.save(content);
        log.info("Content updated successfully");

        return convertToDTO(updatedContent);
    }

    @Transactional
    public void cancelContent(Long id) {
        log.info("Cancelling content ID: {}", id);

        Content content = contentRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Content not found with ID: " + id));

        if (content.getStatus() == ContentStatus.SENT) {
            throw new IllegalArgumentException("Cannot cancel content that has already been sent");
        }

        content.setStatus(ContentStatus.CANCELLED);
        contentRepository.save(content);

        log.info("Content cancelled successfully");
    }

    @Transactional
    public void deleteContent(Long id) {
        log.info("Deleting content ID: {}", id);

        if (!contentRepository.existsById(id)) {
            throw new IllegalArgumentException("Content not found with ID: " + id);
        }

        contentRepository.deleteById(id);
        log.info("Content deleted successfully");
    }

    @Transactional(readOnly = true)
    public List<Content> getDueContent() {
        LocalDateTime now = LocalDateTime.now();
        log.info("Fetching due content for time: {}", now);
        return contentRepository.findDueContent(ContentStatus.SCHEDULED, now);
    }

    private ContentDTO convertToDTO(Content content) {
        ContentDTO dto = new ContentDTO();
        dto.setId(content.getId());
        dto.setSubject(content.getSubject());
        dto.setBody(content.getBody());
        dto.setTopicId(content.getTopic().getId());
        dto.setTopicName(content.getTopic().getName());
        dto.setScheduledTime(content.getScheduledTime());
        dto.setStatus(content.getStatus());
        dto.setSentAt(content.getSentAt());
        return dto;
    }
}
