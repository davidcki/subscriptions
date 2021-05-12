package com.dlozano.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Service;

@Service
public class QueueListener {
  private static final Logger logger = LoggerFactory.getLogger(QueueListener.class);

  @RabbitListener(queues = "new-subscriber-queue")
  void getNewSubscriber(String subscriptionKey) {
    String[] keyParts = subscriptionKey.split("#");
    logger.info("New subscription. Email: {}. Subscription Id: {}.", keyParts[0], keyParts[1]);
    logger.info("Sending welcome email to {} ...", keyParts[0]);
    logger.info("Email to {} was successfully sent.", keyParts[0]);
  }
}
