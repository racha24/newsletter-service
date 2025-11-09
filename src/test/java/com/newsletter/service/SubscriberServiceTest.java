package com.newsletter.service;

import com.newsletter.dto.SubscriberDTO;
import com.newsletter.model.Subscriber;
import com.newsletter.model.Topic;
import com.newsletter.repository.SubscriberRepository;
import com.newsletter.repository.TopicRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SubscriberServiceTest {

    @Mock
    private SubscriberRepository subscriberRepository;

    @Mock
    private TopicRepository topicRepository;

    @InjectMocks
    private SubscriberService subscriberService;

    private Topic testTopic;
    private Subscriber testSubscriber;
    private SubscriberDTO testSubscriberDTO;

    @BeforeEach
    void setUp() {
        testTopic = new Topic();
        testTopic.setId(1L);
        testTopic.setName("Technology");

        testSubscriber = new Subscriber();
        testSubscriber.setId(1L);
        testSubscriber.setName("John Doe");
        testSubscriber.setEmail("john@example.com");
        testSubscriber.setTopic(testTopic);
        testSubscriber.setActive(true);

        testSubscriberDTO = new SubscriberDTO();
        testSubscriberDTO.setName("John Doe");
        testSubscriberDTO.setEmail("john@example.com");
        testSubscriberDTO.setTopicId(1L);
    }

    @Test
    void createSubscriber_Success() {
        // Arrange
        when(topicRepository.findById(1L)).thenReturn(Optional.of(testTopic));
        when(subscriberRepository.existsByEmailAndTopic("john@example.com", testTopic)).thenReturn(false);
        when(subscriberRepository.save(any(Subscriber.class))).thenReturn(testSubscriber);

        // Act
        SubscriberDTO result = subscriberService.createSubscriber(testSubscriberDTO);

        // Assert
        assertNotNull(result);
        assertEquals("John Doe", result.getName());
        assertEquals("john@example.com", result.getEmail());
        assertEquals(1L, result.getTopicId());
        assertTrue(result.getActive());
        verify(subscriberRepository, times(1)).save(any(Subscriber.class));
    }

    @Test
    void createSubscriber_DuplicateEmail_ThrowsException() {
        // Arrange
        when(topicRepository.findById(1L)).thenReturn(Optional.of(testTopic));
        when(subscriberRepository.existsByEmailAndTopic("john@example.com", testTopic)).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> subscriberService.createSubscriber(testSubscriberDTO)
        );

        assertEquals("Subscriber already exists for this topic", exception.getMessage());
        verify(subscriberRepository, never()).save(any(Subscriber.class));
    }

    @Test
    void createSubscriber_TopicNotFound_ThrowsException() {
        // Arrange
        when(topicRepository.findById(999L)).thenReturn(Optional.empty());
        testSubscriberDTO.setTopicId(999L);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> subscriberService.createSubscriber(testSubscriberDTO)
        );

        assertEquals("Topic not found with ID: 999", exception.getMessage());
    }

    @Test
    void getAllSubscribers_Success() {
        // Arrange
        Subscriber subscriber2 = new Subscriber();
        subscriber2.setId(2L);
        subscriber2.setName("Jane Smith");
        subscriber2.setEmail("jane@example.com");
        subscriber2.setTopic(testTopic);
        subscriber2.setActive(true);

        when(subscriberRepository.findAll()).thenReturn(Arrays.asList(testSubscriber, subscriber2));

        // Act
        List<SubscriberDTO> result = subscriberService.getAllSubscribers();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("John Doe", result.get(0).getName());
        assertEquals("Jane Smith", result.get(1).getName());
    }

    @Test
    void getSubscriberById_Success() {
        // Arrange
        when(subscriberRepository.findById(1L)).thenReturn(Optional.of(testSubscriber));

        // Act
        SubscriberDTO result = subscriberService.getSubscriberById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("John Doe", result.getName());
    }

    @Test
    void getSubscribersByTopicId_Success() {
        // Arrange
        when(topicRepository.findById(1L)).thenReturn(Optional.of(testTopic));
        when(subscriberRepository.findByTopicAndActiveTrue(testTopic))
            .thenReturn(Arrays.asList(testSubscriber));

        // Act
        List<SubscriberDTO> result = subscriberService.getSubscribersByTopicId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("John Doe", result.get(0).getName());
    }

    @Test
    void unsubscribe_Success() {
        // Arrange
        when(subscriberRepository.findById(1L)).thenReturn(Optional.of(testSubscriber));
        when(subscriberRepository.save(any(Subscriber.class))).thenReturn(testSubscriber);

        // Act
        subscriberService.unsubscribe(1L);

        // Assert
        verify(subscriberRepository, times(1)).save(argThat(subscriber ->
            !subscriber.getActive()
        ));
    }

    @Test
    void deleteSubscriber_Success() {
        // Arrange
        when(subscriberRepository.existsById(1L)).thenReturn(true);

        // Act
        subscriberService.deleteSubscriber(1L);

        // Assert
        verify(subscriberRepository, times(1)).deleteById(1L);
    }
}
