package com.newsletter.service;

import com.newsletter.dto.TopicDTO;
import com.newsletter.model.Topic;
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
class TopicServiceTest {

    @Mock
    private TopicRepository topicRepository;

    @InjectMocks
    private TopicService topicService;

    private Topic testTopic;
    private TopicDTO testTopicDTO;

    @BeforeEach
    void setUp() {
        testTopic = new Topic();
        testTopic.setId(1L);
        testTopic.setName("Technology");
        testTopic.setDescription("Tech news");

        testTopicDTO = new TopicDTO();
        testTopicDTO.setName("Technology");
        testTopicDTO.setDescription("Tech news");
    }

    @Test
    void createTopic_Success() {
        // Arrange
        when(topicRepository.existsByName("Technology")).thenReturn(false);
        when(topicRepository.save(any(Topic.class))).thenReturn(testTopic);

        // Act
        TopicDTO result = topicService.createTopic(testTopicDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Technology", result.getName());
        assertEquals("Tech news", result.getDescription());
        verify(topicRepository, times(1)).save(any(Topic.class));
    }

    @Test
    void createTopic_DuplicateName_ThrowsException() {
        // Arrange
        when(topicRepository.existsByName("Technology")).thenReturn(true);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> topicService.createTopic(testTopicDTO)
        );

        assertEquals("Topic with name 'Technology' already exists", exception.getMessage());
        verify(topicRepository, never()).save(any(Topic.class));
    }

    @Test
    void getAllTopics_Success() {
        // Arrange
        Topic topic2 = new Topic();
        topic2.setId(2L);
        topic2.setName("Sports");
        topic2.setDescription("Sports news");

        when(topicRepository.findAll()).thenReturn(Arrays.asList(testTopic, topic2));

        // Act
        List<TopicDTO> result = topicService.getAllTopics();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Technology", result.get(0).getName());
        assertEquals("Sports", result.get(1).getName());
    }

    @Test
    void getTopicById_Success() {
        // Arrange
        when(topicRepository.findById(1L)).thenReturn(Optional.of(testTopic));

        // Act
        TopicDTO result = topicService.getTopicById(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals("Technology", result.getName());
    }

    @Test
    void getTopicById_NotFound_ThrowsException() {
        // Arrange
        when(topicRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> topicService.getTopicById(999L)
        );

        assertEquals("Topic not found with ID: 999", exception.getMessage());
    }

    @Test
    void updateTopic_Success() {
        // Arrange
        TopicDTO updateDTO = new TopicDTO();
        updateDTO.setName("Technology Updated");
        updateDTO.setDescription("Updated description");

        when(topicRepository.findById(1L)).thenReturn(Optional.of(testTopic));
        when(topicRepository.existsByName("Technology Updated")).thenReturn(false);
        when(topicRepository.save(any(Topic.class))).thenReturn(testTopic);

        // Act
        TopicDTO result = topicService.updateTopic(1L, updateDTO);

        // Assert
        assertNotNull(result);
        verify(topicRepository, times(1)).save(any(Topic.class));
    }

    @Test
    void deleteTopic_Success() {
        // Arrange
        when(topicRepository.existsById(1L)).thenReturn(true);

        // Act
        topicService.deleteTopic(1L);

        // Assert
        verify(topicRepository, times(1)).deleteById(1L);
    }

    @Test
    void deleteTopic_NotFound_ThrowsException() {
        // Arrange
        when(topicRepository.existsById(999L)).thenReturn(false);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> topicService.deleteTopic(999L)
        );

        assertEquals("Topic not found with ID: 999", exception.getMessage());
        verify(topicRepository, never()).deleteById(any());
    }
}
