package ch.phildev.springphawtrix.web.rest;

import ch.phildev.springphawtrix.config.WebSecurityConfig;
import ch.phildev.springphawtrix.testutils.TestDataBuilderFactory;
import ch.phildev.springphawtrix.web.rest.dto.AnswerDto;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.reactive.WebFluxTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.reactive.server.WebTestClient;

import static org.hamcrest.Matchers.*;

@WebFluxTest
@Slf4j
@Import(WebSecurityConfig.class)
class DrawMatrixResourceTest {

    @Autowired
    private WebTestClient webTestClient;
    @Test
    void printTextOrig() {


        webTestClient.post().uri("/draw/text")
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON)
                .bodyValue(TestDataBuilderFactory.aDefaultDrawText().build())
                .exchange()
                .expectStatus().isOk()
                .expectBody(AnswerDto.class)
                .value(AnswerDto::getPayLoadToMatrix, is(not(blankOrNullString())));
    }
}
