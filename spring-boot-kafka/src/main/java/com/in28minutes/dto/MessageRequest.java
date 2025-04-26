package com.in28minutes.dto;

import jakarta.validation.constraints.NotBlank;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * DTO representing a simple message to be sent to Kafka.
 */
public record MessageRequest(

        @NotBlank(message = "Message cannot be blank")
        @Schema(description = "Message to be published to Kafka", example = "Kafka is awesome!")
        String message

) {}
