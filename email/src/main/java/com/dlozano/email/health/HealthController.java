package com.dlozano.email.health;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HealthController {

    @GetMapping("/health")
    String getStatus() {
        return "Email Microservice is running.";
    }
}
