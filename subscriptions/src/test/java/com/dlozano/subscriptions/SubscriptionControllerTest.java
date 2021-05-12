package com.dlozano.subscriptions;

import com.dlozano.subscriptions.model.Subscription;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class SubscriptionControllerTest {

  private static final String SUBSCRIPTION_PATH = "/subscription";
  private static final String SUBSCRIPTION_ID = "609bc7d1418d3753487b623f";
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

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private SubscriptionService subscriptionService;

  private static final ObjectMapper mapper = new ObjectMapper();

  @Mock
  private Subscription subscription;

  @BeforeEach
  void setUp() {
    subscription = mock(Subscription.class);
    when(subscription.getId()).thenReturn(SUBSCRIPTION_ID);
  }

  @Test
  void createSubscription_validRequest_shouldReturnTheUriOfTheSubscription() throws Exception {
    when(subscriptionService.save(any(Subscription.class))).thenReturn(subscription);
    String requestAsJson = mapper.writeValueAsString(BASE_REQUEST);

    mockMvc
        .perform(
            post(SUBSCRIPTION_PATH).contentType(MediaType.APPLICATION_JSON).content(requestAsJson))
        .andExpect(status().isCreated())
        .andExpect(header().string("Location", String.format("subscription/%s", SUBSCRIPTION_ID)));
  }

  @Test
  void createSubscription_invalidGender_shouldReturnErrorMessage() throws Exception {
    Map<String, Object> request = new HashMap<>(BASE_REQUEST);
    request.put(GENDER, "Masculine");
    String requestAsJson = mapper.writeValueAsString(request);

    mockMvc
        .perform(
            post(SUBSCRIPTION_PATH).contentType(MediaType.APPLICATION_JSON).content(requestAsJson))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("'Masculine' is not a valid gender."));
  }

  @Test
  void createSubmission_dateOfBirthIsNotInIsoFormat_shouldReturnErrorMessage() throws Exception {
    Map<String, Object> request = new HashMap<>(BASE_REQUEST);
    request.put(DATE_OF_BIRTH, "10-20-2000");
    String requestAsJson = mapper.writeValueAsString(request);

    mockMvc
        .perform(
            post(SUBSCRIPTION_PATH).contentType(MediaType.APPLICATION_JSON).content(requestAsJson))
        .andExpect(status().isBadRequest())
        .andExpect(content().string("'10-20-2000' is not a valid date of birth."));
  }

  @Test
  void createSubscription_missingRequiredAttributes_shouldReturnErrorMessages() throws Exception {
    String requestAsJson = mapper.writeValueAsString(new HashMap<>());

    String responseContent =
        mockMvc
            .perform(
                post(SUBSCRIPTION_PATH)
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(requestAsJson))
            .andExpect(status().isBadRequest())
            .andReturn()
            .getResponse()
            .getContentAsString();

    String[] errorMessages = responseContent.split("\\.");
    assertArrayEquals(
        errorMessages,
        new String[] {
          "'null' is not a valid email address",
          "'null' is not a valid date of birth",
          "'null' is not a valid consent indicator",
          "'null' is not a valid newsletter id"
        });
  }
}