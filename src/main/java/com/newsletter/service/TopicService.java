package com.newsletter.service;

import com.newsletter.dto.TopicDTO;
import com.newsletter.model.Topic;
import com.newsletter.repository.TopicRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class TopicService {

    private final TopicRepository topicRepository;

    @Transactional
    public TopicDTO createTopic(TopicDTO topicDTO) {
        log.info("Creating topic: {}", topicDTO.getName());

        if (topicRepository.existsByName(topicDTO.getName())) {
            throw new IllegalArgumentException("Topic with name '" + topicDTO.getName() + "' already exists");
        }

        Topic topic = new Topic();
        topic.setName(topicDTO.getName());
        topic.setDescription(topicDTO.getDescription());

        Topic savedTopic = topicRepository.save(topic);
        log.info("Topic created successfully with ID: {}", savedTopic.getId());

        return convertToDTO(savedTopic);
    }

    @Transactional(readOnly = true)
    public List<TopicDTO> getAllTopics() {
        log.info("Fetching all topics");
        return topicRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public TopicDTO getTopicById(Long id) {
        log.info("Fetching topic by ID: {}", id);
        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Topic not found with ID: " + id));
        return convertToDTO(topic);
    }

    @Transactional
    public TopicDTO updateTopic(Long id, TopicDTO topicDTO) {
        log.info("Updating topic ID: {}", id);

        Topic topic = topicRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Topic not found with ID: " + id));

        if (!topic.getName().equals(topicDTO.getName()) &&
                topicRepository.existsByName(topicDTO.getName())) {
            throw new IllegalArgumentException("Topic with name '" + topicDTO.getName() + "' already exists");
        }

        topic.setName(topicDTO.getName());
        topic.setDescription(topicDTO.getDescription());

        Topic updatedTopic = topicRepository.save(topic);
        log.info("Topic updated successfully");

        return convertToDTO(updatedTopic);
    }

    @Transactional
    public void deleteTopic(Long id) {
        log.info("Deleting topic ID: {}", id);

        if (!topicRepository.existsById(id)) {
            throw new IllegalArgumentException("Topic not found with ID: " + id);
        }

        topicRepository.deleteById(id);
        log.info("Topic deleted successfully");
    }

    private TopicDTO convertToDTO(Topic topic) {
        TopicDTO dto = new TopicDTO();
        dto.setId(topic.getId());
        dto.setName(topic.getName());
        dto.setDescription(topic.getDescription());
        return dto;
    }
}
