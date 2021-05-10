package com.dlozano.publicapi.web;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
class HealthController {

    @GetMapping("/health")
    String getStatus() {
        return "Public API Microservice is running.";
    }
}
