package ch.phildev.springphawtrix.web.rest;

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

import ch.phildev.springphawtrix.app.management.PhawtrixAppManager;
import ch.phildev.springphawtrix.communicator.ConnectToMatrixHandler;
import ch.phildev.springphawtrix.domain.PhawtrixMqttConfig;

@RestController
@Slf4j
@RequestMapping(value = "/app",
        consumes = MediaType.APPLICATION_JSON_VALUE,
        produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class AppResource {

    private final PhawtrixAppManager appManager;
    private final ConnectToMatrixHandler connectToMatrixHandler;
    private final Mqtt3ReactorClient client;
    private final PhawtrixMqttConfig cfg;

    @GetMapping(value = "/command/{appName}/execute",
            produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    public Flux<String> executeApp(@PathVariable String appName) {
        Assert.hasText(appName, "AppName must be given");
        log.debug("reached the controller");

        return connectToMatrixHandler.connectScenario()
                .checkpoint()
                .then(appManager.initApp(appName))
                .checkpoint()
                .flux()
                .zipWith(appManager.executeApp(appName))
                .map(t -> t.getT2().toString());
//                .flatMapIterable(MatrixFrame::getFrameBuffer)
//                .map(frameBytes -> Mqtt3Publish.builder()
//                        .topic(cfg.getMatrixPublishTopic())
//                        .qos(MqttQos.AT_LEAST_ONCE)
//                        .payload(ByteBuffer.wrap(frameBytes))
//                        .build())
//                .publish(client::publish)
//                .doOnNext(pub -> log.debug("Publish result: {}", pub.getPublish()))
//                .transformDeferred(PublishToMatrixHandler::convertPublishResultsToReadableString);
    }
}
