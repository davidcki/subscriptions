## Subscriptions system

### Description

This application models a newsletter subscription system.

It contains three main parts:
* A Public Service: A microservice intended to be used by a frontend, or other type of client.
* A Subscription Service: A microservice implementing subscription logic, including persistence of subscription data in a database and email notification to confirm process is complete.
* An Email Service: A microservice implementing email notifications. This is a stubbed implementation.

### Technologies

* Gradle: Used for dependency and project management.
* Docker: Each microservice, as well as the other supporting software (MongoDB and RabbitMQ) are configured to run in Docker containers.
* Docker-compose: Built on top of Docker. Easily allows to treat the containers as a composed entity. Starting them and stopping them when needed, as well as enabling network communication between containers.
* Spring Boot: A DI framework used to develop microservices. Spring subprojects used in this application include Spring Web, Spring Data for MongoDB and Spring AMQP.

### Prerequisites

* Install Docker. Installations instructions available [here](https://docs.docker.com/get-docker/).
* Install docker-compose: Installations instructions available [here](https://docs.docker.com/compose/install/).

### Execution instructions

While in the project's root folder:

`docker-compose up -d` will start the five containers that compose this application.

`docker-compose down` will stop and delete the containers created by the command above.

`docker-compose logs -f` will tail on the containers logs.

### Testing the application

Using CURL to invoke the endpoints (currently only new subscription) in the public service.

```
curl -X POST                                            \
        -H "Content-type: application/json"             \
        -d "{                                           \
            \"email\": \"user@company.com\",            \
            \"date_of_birth\": \"1950-07-31\",          \
            \"consent\": \"true\",                      \
            \"newsletter_id\": \"78871289090\",         \
            \"first_name\": \"John\",                   \
            \"gender\": \"MAN\"                         \
        }"                                              \
        "http://localhost:10010/api/subscription"
```