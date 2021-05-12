package com.dlozano.subscriptions;

import com.dlozano.subscriptions.model.Subscription;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.regex.Pattern;

/** A web controller for CRUD operations on {@link Subscription} entities. */
@RestController
class SubscriptionController {
  private static final Pattern EMAIL_REGEX = Pattern.compile("^[A-Za-z0-9+_.-]+@(.+)$");

  private final SubscriptionService subscriptionService;

  @Autowired
  SubscriptionController(SubscriptionService subscriptionService) {
    this.subscriptionService = subscriptionService;
  }

  @PostMapping(value = "/subscription", headers = "Accept=application/json")
  ResponseEntity<String> createSubscription(
      @RequestBody Map<String, Object> subscriptionAttributes) {
    List<String> validations = validateSubscriptionForCreation(subscriptionAttributes);
    if (!validations.isEmpty()) {
      return ResponseEntity.badRequest().body(validations.stream().reduce("", String::concat));
    }

    Subscription subscription =
        subscriptionService.save(buildSubscriptionForCreation(subscriptionAttributes));
    return ResponseEntity.created(
            URI.create(String.format("subscription/%s", subscription.getId())))
        .build();
  }

  private static Subscription buildSubscriptionForCreation(
      Map<String, Object> subscriptionAttributes) {
    Subscription.Builder builder =
        new Subscription.Builder(
            subscriptionAttributes.get("email").toString(),
            LocalDate.parse(subscriptionAttributes.get("date_of_birth").toString()),
            Boolean.parseBoolean(subscriptionAttributes.get("consent").toString()),
            subscriptionAttributes.get("newsletter_id").toString());

    Optional.ofNullable(subscriptionAttributes.get("first_name"))
        .ifPresent(firstName -> builder.firstName(firstName.toString()));
    Optional.ofNullable(subscriptionAttributes.get("gender"))
        .ifPresent(gender -> builder.gender(Subscription.Gender.valueOf(gender.toString())));
    return builder.build();
  }

  private static List<String> validateSubscriptionForCreation(
      Map<String, Object> subscriptionAttributes) {
    List<String> messages = new ArrayList<>();
    if (subscriptionAttributes.get("email") == null
        || !EMAIL_REGEX.matcher(subscriptionAttributes.get("email").toString()).matches()) {
      messages.add(
          String.format("'%s' is not a valid email address.", subscriptionAttributes.get("email")));
    }

    try {
      LocalDate.parse(subscriptionAttributes.get("date_of_birth").toString());
    } catch (Exception e) {
      messages.add(
          String.format(
              "'%s' is not a valid date of birth.", subscriptionAttributes.get("date_of_birth")));
    }

    if (subscriptionAttributes.get("consent") == null) {
      messages.add(
          String.format(
              "'%s' is not a valid consent indicator.", subscriptionAttributes.get("consent")));
    }

    if (subscriptionAttributes.get("newsletter_id") == null) {
      messages.add(
          String.format(
              "'%s' is not a valid newsletter id.", subscriptionAttributes.get("newsletter_id")));
    }

    if (subscriptionAttributes.get("gender") != null) {
      try {
        Subscription.Gender.valueOf(subscriptionAttributes.get("gender").toString());
      } catch (Exception e) {
        messages.add(
            String.format("'%s' is not a valid gender.", subscriptionAttributes.get("gender")));
      }
    }

    return messages;
  }
}
