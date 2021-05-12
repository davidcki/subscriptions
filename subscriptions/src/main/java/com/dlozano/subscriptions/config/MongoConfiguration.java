package com.dlozano.subscriptions.config;

import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@EnableMongoRepositories(basePackages = "com.dlozano.subscriptions")
class MongoConfiguration {
}
