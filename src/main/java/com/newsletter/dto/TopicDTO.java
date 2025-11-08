package com.newsletter.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TopicDTO {

    private Long id;

    @NotBlank(message = "Topic name is required")
    private String name;

    private String description;
}
