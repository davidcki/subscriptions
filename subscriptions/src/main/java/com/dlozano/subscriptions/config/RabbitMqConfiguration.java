package com.dlozano.subscriptions.config;

import org.springframework.amqp.core.Queue;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
class RabbitMqConfiguration {

  @Bean
  Queue newSubscriberQueue() {
    return new Queue("new-subscriber-queue");
  }
}
