package com.dlozano.subscriptions;

import com.dlozano.subscriptions.model.Subscription;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Handles business operations on {@link Subscription} entities. */
@Service
class SubscriptionService {

  private final SubscriptionRepository subscriptionRepository;
  private final RabbitTemplate rabbitTemplate;

  @Autowired
  SubscriptionService(SubscriptionRepository repository, RabbitTemplate rabbitTemplate) {
    this.subscriptionRepository = repository;
    this.rabbitTemplate = rabbitTemplate;
  }

  Subscription save(Subscription subscription) {
    Subscription newSubscription = subscriptionRepository.save(subscription);
    rabbitTemplate.convertAndSend(
        "new-subscriber-queue",
        String.format("%s#%s", newSubscription.getEmail(), newSubscription.getId()));
    return newSubscription;
  }
}
