package com.in28minutes.config;

import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Value("${rabbitmq.queue-name}")
    private String queueName;

    @Value("${rabbitmq.exchange-name}")
    private String exchangeName;

    @Value("${rabbitmq.routing-key}")
    private String routingKey;

    @Bean
    public Queue myQueue() {
        return new Queue(queueName, true,false, false); // Durable
    }

    @Bean
    public DirectExchange exchange() {
        return new DirectExchange(exchangeName, true, false); // Durable
    }

    @Bean
    public Binding binding(Queue myQueue, DirectExchange exchange) {
        return BindingBuilder.bind(myQueue).to(exchange).with(routingKey);
    }

    // -----------------------------------------
    // Fanout Exchange Example (Broadcasting)
    // -----------------------------------------
    @Bean
    public FanoutExchange fanoutExchange() {
        return new FanoutExchange("fanout.exchange");
    }

    // Example queue for fanout
    @Bean
    public Queue fanoutQueue() {
        return new Queue("fanout.queue");
    }

    @Bean
    public Binding fanoutBinding(FanoutExchange fanoutExchange, Queue fanoutQueue) {
        return BindingBuilder.bind(fanoutQueue).to(fanoutExchange);
    }

    // -----------------------------------------
    // Topic Exchange Example (Pattern Routing)
    // -----------------------------------------
    @Bean
    public TopicExchange topicExchange() {
        return new TopicExchange("topic.exchange");
    }

    @Bean
    public Queue topicQueue() {
        return new Queue("topic.queue");
    }

    @Bean
    public Binding topicBinding(TopicExchange topicExchange, Queue topicQueue) {
        return BindingBuilder.bind(topicQueue).to(topicExchange).with("user.*");
    }
}
