package ch.phildev.springphawtrix.web.rest;

import ch.phildev.springphawtrix.app.management.PhawtrixAppManager;
import ch.phildev.springphawtrix.communicator.ConnectToMatrixHandler;
import ch.phildev.springphawtrix.communicator.PublishToMatrixHandler;
import ch.phildev.springphawtrix.domain.MatrixFrame;
import ch.phildev.springphawtrix.domain.PhawtrixMqttConfig;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish;
import com.hivemq.client.mqtt.mqtt3.reactor.Mqtt3ReactorClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.nio.ByteBuffer;

@RestController
@Slf4j
@RequestMapping(value = "/app",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AppResource {

    private final PhawtrixAppManager appManager;
    private final ConnectToMatrixHandler connectToMatrixHandler;

    @GetMapping(value = "/command/{appName}/execute", consumes = MediaType.ALL_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public Flux<String> executeApp(@PathVariable String appName) {
        Assert.hasText(appName, "AppName must be given");
        log.debug("reached the controller");

        return connectToMatrixHandler.connectScenario()
                .checkpoint()
                .then(appManager.initApp(appName))
                .checkpoint()
                .thenMany(appManager.executeApp(appName))
                .map(t -> t.toString())
                .doOnNext(pub -> log.debug(pub));
    }

    @GetMapping(value = "/command/{appName}/executeAndForget",
            consumes = MediaType.ALL_VALUE,
            produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<String> executeAppAsync(@PathVariable String appName) {

        Assert.hasText(appName, "AppName must be given");
        log.debug("reached the controller");

        return connectToMatrixHandler.connectScenario()
                .checkpoint()
                .then(appManager.initApp(appName))
                .doOnSuccess(data -> log.debug("App is initialized"))
                .checkpoint()
                .doOnSuccess(data -> appManager.executeApp(appName)
                        .subscribe())
                .map(sub -> "Successfully fired");
    }
}
