package com.newsletter.service;

import com.newsletter.dto.SubscriberDTO;
import com.newsletter.model.Subscriber;
import com.newsletter.model.Topic;
import com.newsletter.repository.SubscriberRepository;
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
public class SubscriberService {

    private final SubscriberRepository subscriberRepository;
    private final TopicRepository topicRepository;

    @Transactional
    public SubscriberDTO createSubscriber(SubscriberDTO subscriberDTO) {
        log.info("Creating subscriber: {} for topic ID: {}", subscriberDTO.getEmail(), subscriberDTO.getTopicId());

        Topic topic = topicRepository.findById(subscriberDTO.getTopicId())
                .orElseThrow(() -> new IllegalArgumentException("Topic not found with ID: " + subscriberDTO.getTopicId()));

        if (subscriberRepository.existsByEmailAndTopic(subscriberDTO.getEmail(), topic)) {
            throw new IllegalArgumentException("Subscriber already exists for this topic");
        }

        Subscriber subscriber = new Subscriber();
        subscriber.setName(subscriberDTO.getName());
        subscriber.setEmail(subscriberDTO.getEmail());
        subscriber.setTopic(topic);
        subscriber.setActive(true);

        Subscriber savedSubscriber = subscriberRepository.save(subscriber);
        log.info("Subscriber created successfully with ID: {}", savedSubscriber.getId());

        return convertToDTO(savedSubscriber);
    }

    @Transactional(readOnly = true)
    public List<SubscriberDTO> getAllSubscribers() {
        log.info("Fetching all subscribers");
        return subscriberRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public SubscriberDTO getSubscriberById(Long id) {
        log.info("Fetching subscriber by ID: {}", id);
        Subscriber subscriber = subscriberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subscriber not found with ID: " + id));
        return convertToDTO(subscriber);
    }

    @Transactional(readOnly = true)
    public List<SubscriberDTO> getSubscribersByTopicId(Long topicId) {
        log.info("Fetching subscribers for topic ID: {}", topicId);
        Topic topic = topicRepository.findById(topicId)
                .orElseThrow(() -> new IllegalArgumentException("Topic not found with ID: " + topicId));

        return subscriberRepository.findByTopicAndActiveTrue(topic).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public SubscriberDTO updateSubscriber(Long id, SubscriberDTO subscriberDTO) {
        log.info("Updating subscriber ID: {}", id);

        Subscriber subscriber = subscriberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subscriber not found with ID: " + id));

        subscriber.setName(subscriberDTO.getName());
        subscriber.setEmail(subscriberDTO.getEmail());

        if (subscriberDTO.getActive() != null) {
            subscriber.setActive(subscriberDTO.getActive());
        }

        Subscriber updatedSubscriber = subscriberRepository.save(subscriber);
        log.info("Subscriber updated successfully");

        return convertToDTO(updatedSubscriber);
    }

    @Transactional
    public void unsubscribe(Long id) {
        log.info("Unsubscribing subscriber ID: {}", id);

        Subscriber subscriber = subscriberRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Subscriber not found with ID: " + id));

        subscriber.setActive(false);
        subscriberRepository.save(subscriber);

        log.info("Subscriber unsubscribed successfully");
    }

    @Transactional
    public void deleteSubscriber(Long id) {
        log.info("Deleting subscriber ID: {}", id);

        if (!subscriberRepository.existsById(id)) {
            throw new IllegalArgumentException("Subscriber not found with ID: " + id);
        }

        subscriberRepository.deleteById(id);
        log.info("Subscriber deleted successfully");
    }

    private SubscriberDTO convertToDTO(Subscriber subscriber) {
        SubscriberDTO dto = new SubscriberDTO();
        dto.setId(subscriber.getId());
        dto.setName(subscriber.getName());
        dto.setEmail(subscriber.getEmail());
        dto.setTopicId(subscriber.getTopic().getId());
        dto.setTopicName(subscriber.getTopic().getName());
        dto.setActive(subscriber.getActive());
        return dto;
    }
}
