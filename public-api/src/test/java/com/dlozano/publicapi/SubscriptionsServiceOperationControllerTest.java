package com.dlozano.publicapi;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.web.client.RestTemplate;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class SubscriptionsServiceOperationControllerTest {

  private static final String SUBSCRIPTION_PATH = "/api/subscription";
  private static final String SUBSCRIPTION_ID = "609c230dcb29fb7679110233";
  private static final String EMAIL = "email";
  private static final String DATE_OF_BIRTH = "date_of_birth";
  private static final String CONSENT = "consent";
  private static final String NEWSLETTER_ID = "newsletter_id";
  private static final String FIRST_NAME = "first_name";
  private static final String GENDER = "gender";

  private static final Map<String, Object> BASE_REQUEST =
    Map.of(
      EMAIL, "user@mail.com",
      DATE_OF_BIRTH, "2000-10-20",
      CONSENT, "true",
      NEWSLETTER_ID, "Q1_2021",
      FIRST_NAME, "John",
      GENDER, "MAN");

  @MockBean
  private RestTemplate restTemplate;

  @Autowired
  private MockMvc mockMvc;

  private static final ObjectMapper mapper = new ObjectMapper();

  @Test
  void
      createSubscription_creationWasSuccessfulInSubscriptionService_shouldReturnTheSameObtainedResponse()
          throws Exception {
    when(restTemplate.postForEntity(anyString(), eq(BASE_REQUEST), any(Class.class)))
        .thenReturn(
            ResponseEntity.created(URI.create(String.format("subscription/%s", SUBSCRIPTION_ID)))
                .build());
    String requestAsJson = mapper.writeValueAsString(BASE_REQUEST);

    mockMvc
        .perform(
            post(SUBSCRIPTION_PATH).contentType(MediaType.APPLICATION_JSON).content(requestAsJson))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", String.format("subscription/%s", SUBSCRIPTION_ID)));
  }

  @Test
  void createSubscription_creationFailedInSubscriptionService_shouldReturnTheSameObtainedResponse()
      throws Exception {
    Map<String, Object> request = new HashMap<>(BASE_REQUEST);
    request.put(DATE_OF_BIRTH, "24-30-2010");
    when(restTemplate.postForEntity(anyString(), eq(request), any(Class.class)))
        .thenReturn(ResponseEntity.badRequest().body("'24-30-2010' is not a valid date of birth."));
    String requestAsJson = mapper.writeValueAsString(request);

    mockMvc
        .perform(
            post(SUBSCRIPTION_PATH).contentType(MediaType.APPLICATION_JSON).content(requestAsJson))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("'24-30-2010' is not a valid date of birth."));
  }
}