package com.dlozano.subscriptions.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HealthController {

    @GetMapping("/health")
    String getStatus() {
        return "Subscriptions Microservice is running.";
    }
}
