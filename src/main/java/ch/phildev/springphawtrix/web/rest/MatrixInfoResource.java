package ch.phildev.springphawtrix.web.rest;

import ch.phildev.springphawtrix.communicator.ConnectToMatrixHandler;
import ch.phildev.springphawtrix.communicator.MatrixStreamHolder;
import ch.phildev.springphawtrix.communicator.PublishToMatrixHandler;
import ch.phildev.springphawtrix.domain.PhawtrixCommand;
import ch.phildev.springphawtrix.domain.PhawtrixMqttConfig;
import ch.phildev.springphawtrix.service.CommandEncoder;
import ch.phildev.springphawtrix.web.rest.dto.MatrixInfoDto;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@RestController
@RequestMapping(value = "/matrix-info",
        produces = MediaType.APPLICATION_JSON_VALUE)
@Slf4j
public class MatrixInfoResource {

    private final PhawtrixMqttConfig cfg;
    private final ConnectToMatrixHandler connectHandler;
    private final PublishToMatrixHandler publishHandler;
    private final MatrixStreamHolder streamHolder;
    private final CommandEncoder commandEncoder;


    public MatrixInfoResource(PhawtrixMqttConfig cfg,
                              ConnectToMatrixHandler connectHandler,
                              PublishToMatrixHandler publishHandler,
                              MatrixStreamHolder streamHolder,
                              CommandEncoder commandEncoder) {
        this.cfg = cfg;
        this.connectHandler = connectHandler;
        this.publishHandler = publishHandler;
        this.streamHolder = streamHolder;
        this.commandEncoder = commandEncoder;
    }

    /**
     * Get the current status information from the connected matrix
     *
     * @return an object with the status information or if it fails an error code
     */
    @GetMapping(value = "/", consumes = MediaType.ALL_VALUE)
    @Operation(summary = "Return current status from matrix",
            description = """
                    Return an object with a set of information from the matrix.
                    The information include info on the:
                    * wifi
                    * the version
                    * the ip-address
                    * measured brightness
                    * the measured temperature
                    * the measured pressure
                    * the measured humidity.""")
    @ApiResponse(responseCode = "200", description = "Matrix info returned.")
    @ApiResponse(responseCode = "502", description = "Error during JSON processing.")
    public Mono<MatrixInfoDto> getMatrixInfo() {
        Flux<byte[]> payload = Flux.just(commandEncoder.getPayloadForMatrix(PhawtrixCommand.GET_MATRIX_INFO));

        Flux<Mqtt3Publish> infoStreamFromMatrix = Flux.defer(() ->
                streamHolder.getStreamHolder()
                        .orElse(Flux.error(new RuntimeException("No subscription")))
        );

        // getPayload
        return connectHandler.connectScenario()
                .then(
                        publishHandler.publishScenario(payload).doOnSuccess(bytes -> log.debug("Published finished")))
                .thenMany(infoStreamFromMatrix
                        .map(Mqtt3Publish::getPayloadAsBytes))
                .map(String::new)
                .filter(s -> s.contains("MatrixInfo"))
                .takeUntil(s -> s.contains("MatrixInfo"))
                .last()
                .map(s -> new Gson().fromJson(s.toLowerCase(), MatrixInfoDto.class))
                .onErrorMap(JsonSyntaxException.class, e -> new ResponseStatusException(HttpStatus.BAD_GATEWAY, "Error " +
                        "during JSON processing", e))
                .timeout(Duration.ofSeconds(cfg.getTimeoutMs()));
    }
}
