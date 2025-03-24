package com.example.file_processor.configurations;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class RabbitMqConfiguration {
    public static final String QUEUE_NAME = "myQueue";

    @Bean
    public Queue queue() {
        return new Queue(QUEUE_NAME, false);
    }
}
