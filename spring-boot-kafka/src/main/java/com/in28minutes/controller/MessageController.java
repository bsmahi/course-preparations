package com.in28minutes.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.web.bind.annotation.RequestBody;
import com.in28minutes.dto.MessageRequest;
import com.in28minutes.producer.KafkaProducerService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class MessageController {

    private final KafkaProducerService kafkaProducer;

    public MessageController(KafkaProducerService kafkaProducer) {
        this.kafkaProducer = kafkaProducer;
    }

    /**
     * REST endpoint to send a message to Kafka topic.
     *
     * @param request MessageRequest payload containing the message string
     * @return HTTP 201 Created response with success info
     */
    @Operation(summary = "Send a message to Kafka topic")
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Message accepted for delivery",
                    content = @Content(schema = @Schema(implementation = Map.class))
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Invalid input",
                    content = @Content
            )
    })
    @PostMapping("/produce")
    public ResponseEntity<Map<String, Object>> sendMessage(
            @io.swagger.v3.oas.annotations.parameters.RequestBody(
                    description = "Message payload",
                    required = true,
                    content = @Content(schema = @Schema(implementation = MessageRequest.class))
            )
            @Valid @RequestBody MessageRequest request

    ) {
        kafkaProducer.sendMessage(request.message());

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(Map.of(
                        "statusCode", HttpStatus.CREATED,
                        "info", "Message sent to Kafka successfully"
                ));
    }
}
