package com.newsletter.dto;

import com.newsletter.model.Content.ContentStatus;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ContentDTO {

    private Long id;

    @NotBlank(message = "Subject is required")
    private String subject;

    @NotBlank(message = "Body is required")
    private String body;

    @NotNull(message = "Topic ID is required")
    private Long topicId;

    private String topicName;

    @NotNull(message = "Scheduled time is required")
    private LocalDateTime scheduledTime;

    private ContentStatus status;

    private LocalDateTime sentAt;
}
