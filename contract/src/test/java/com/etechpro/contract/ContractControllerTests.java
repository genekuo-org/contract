package com.etechpro.contract;

import static org.springframework.boot.test.context.SpringBootTest.WebEnvironment.RANDOM_PORT;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNPROCESSABLE_ENTITY;
import static org.springframework.http.MediaType.APPLICATION_JSON;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.reactive.server.WebTestClient;

@SpringBootTest(webEnvironment = RANDOM_PORT)
class ContractControllerTests {

    @Autowired private WebTestClient client;

    @Test
    void getContractById() {

        int contractId = 1;

        client.get()
                .uri("/contract/" + contractId)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.contractId").isEqualTo(contractId);
    }

    @Test
    void getContractInvalidParameterString() {

        client.get()
                .uri("/contract/no-integer")
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(BAD_REQUEST)
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("/contract/no-integer")
                .jsonPath("$.message").isEqualTo("Type mismatch.");
    }

    @Test
    void getContractNotFound() {

        int contractIdNotFound = 13;

        client.get()
                .uri("/contract/" + contractIdNotFound)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isNotFound()
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("/contract/" + contractIdNotFound)
                .jsonPath("$.message").isEqualTo("No contract found for contractId: " + contractIdNotFound);
    }

    @Test
    void getContractInvalidParameterNegativeValue() {

        int contractIdInvalid = -1;

        client.get()
                .uri("/contract/" + contractIdInvalid)
                .accept(APPLICATION_JSON)
                .exchange()
                .expectStatus().isEqualTo(UNPROCESSABLE_ENTITY)
                .expectHeader().contentType(APPLICATION_JSON)
                .expectBody()
                .jsonPath("$.path").isEqualTo("/contract/" + contractIdInvalid)
                .jsonPath("$.message").isEqualTo("Invalid contractId: " + contractIdInvalid);
    }
}
