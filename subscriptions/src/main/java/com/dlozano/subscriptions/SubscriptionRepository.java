package com.dlozano.subscriptions;

import com.dlozano.subscriptions.model.Subscription;
import org.springframework.data.mongodb.repository.MongoRepository;

/** A {@link MongoRepository} to access {@link Subscription} entities. */
interface SubscriptionRepository extends MongoRepository<Subscription, String> {}
