package com.in28minutes.handler;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;

import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;

@Component
public class AsyncMessageHandler {

    private static final Logger log = LoggerFactory.getLogger(AsyncMessageHandler.class);

    private final BlockingQueue<String> messages;

    public AsyncMessageHandler() {
        this.messages = new LinkedBlockingQueue<>();
    }

    @RabbitListener(queues = "${rabbitmq.queue-name}")
    public void receiveMessage(String message) {
        log.info("Received message: {}", message);
        messages.add(message);
    }

    public String consumeMessage() {
        return messages.poll();
    }
}
