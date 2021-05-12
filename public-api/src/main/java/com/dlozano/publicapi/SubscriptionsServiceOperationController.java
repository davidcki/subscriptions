package com.dlozano.publicapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.HttpStatusCodeException;
import org.springframework.web.client.RestTemplate;

import java.util.Map;

/** A web controller to invoke endpoints on the Subscriptions service */
@RestController
@RequestMapping("/api")
public class SubscriptionsServiceOperationController {

  private final RestTemplate restTemplate;
  private final String createSubscriptionsEndpointUrl;

  @Autowired
  SubscriptionsServiceOperationController(
      RestTemplate restTemplate,
      @Value("${subscriptions.service.url}") String subscriptionsServiceUrl) {
    this.restTemplate = restTemplate;
    this.createSubscriptionsEndpointUrl =
        String.format("%s/%s", subscriptionsServiceUrl, "/subscription");
  }

  @PostMapping(value = "/subscription", headers = "Accept=application/json")
  ResponseEntity<String> createSubscription(
      @RequestBody Map<String, Object> subscriptionAttributes) {

    try {
      return restTemplate.postForEntity(
          createSubscriptionsEndpointUrl, subscriptionAttributes, String.class);
    } catch (HttpStatusCodeException e) {
      return ResponseEntity.status(e.getStatusCode())
          .headers(e.getResponseHeaders())
          .body(e.getResponseBodyAsString());
    }
  }
}
