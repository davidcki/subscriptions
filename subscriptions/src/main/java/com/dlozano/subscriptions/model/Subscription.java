package com.dlozano.subscriptions.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.lang.NonNull;

import java.time.LocalDate;
import java.util.Optional;

/**
 * A a user subscription to a certain newsletter that can be saved in MongoDB.
 */
@Document(collection = "subscriptions")
public class Subscription {

    @Id
    private String id;

    private final String email;

    private final String firstName;

    private final Gender gender;

    private final LocalDate dateOfBirth;

    private final boolean consentFlag;

    private final String newsletterId;

    @NonNull
    public String getId() {
        return id;
    }

    @NonNull
    public String getEmail() {
        return email;
    }

    public Optional<String> getFirstName() {
        return Optional.ofNullable(firstName);
    }

    public Optional<Gender> getGender() {
        return Optional.ofNullable(gender);
    }

    @NonNull
    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public boolean isConsentFlag() {
        return consentFlag;
    }

    @NonNull
    public String getNewsletterId() {
        return newsletterId;
    }

    private Subscription(Builder builder) {
        this.email = builder.email;
        this.firstName = builder.firstName;
        this.gender = builder.gender;
        this.dateOfBirth = builder.dateOfBirth;
        this.consentFlag = builder.consentFlag;
        this.newsletterId = builder.newsletterId;
    }

    public enum Gender {
        MAN,
        WOMAN,
        NON_BINARY
    }

    /**
     * A builder used to construct objects of type {@link Subscription}.
     */
    public static class Builder {
        private final String email;

        private String firstName;

        private Gender gender;

        private final LocalDate dateOfBirth;

        private final boolean consentFlag;

        private final String newsletterId;

        public Builder(String email, LocalDate dateOfBirth, boolean consentFlag, String newsletterId) {
            this.email = email;
            this.dateOfBirth = dateOfBirth;
            this.consentFlag = consentFlag;
            this.newsletterId = newsletterId;
        }

        public Builder firstName(String firstName) {
            this.firstName = firstName;
            return this;
        }

        public Builder gender(Gender gender) {
            this.gender = gender;
            return this;
        }

        public Subscription build() {
            return new Subscription(this);
        }
    }
}
