package ch.phildev.springphawtrix.web.rest;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.test.web.reactive.server.WebTestClient;


@WebFluxTest
@Slf4j

class AppResourceTest {

    @Autowired
    private WebTestClient webTestClient;

    @Test
    void testIfTextStreamHeaderIsAccepted(){
        webTestClient.get().uri("/app/command/SimpleClock/execute")
//                .accept(MediaType.APPLICATION_JSON)
                .exchange()
                .expectStatus().isOk();
    }

}