package com.dlozano.subscriptions;

import com.dlozano.subscriptions.model.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/** Handles business operations on {@link Subscription} entities. */
@Service
class SubscriptionService {

  private final SubscriptionRepository subscriptionRepository;

  @Autowired
  SubscriptionService(SubscriptionRepository repository) {
    this.subscriptionRepository = repository;
  }

  Subscription save(Subscription subscription) {
    // TODO send email
    return subscriptionRepository.save(subscription);
  }
}
