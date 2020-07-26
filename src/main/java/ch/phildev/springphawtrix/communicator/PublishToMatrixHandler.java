package ch.phildev.springphawtrix.communicator;

import javax.validation.constraints.NotNull;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.stream.IntStream;

import com.google.common.io.BaseEncoding;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish;
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3PublishResult;
import com.hivemq.client.mqtt.mqtt3.reactor.Mqtt3ReactorClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import ch.phildev.springphawtrix.domain.PhawtrixMqttConfig;

@Component
@Slf4j
public class PublishToMatrixHandler {
    private final PhawtrixMqttConfig cfg;

    private final @NotNull Mqtt3ReactorClient client;

    public PublishToMatrixHandler(PhawtrixMqttConfig cfg,
                                  @NotNull Mqtt3ReactorClient client) {
        this.cfg = cfg;
        this.client = client;
    }

    /**
     * Publish scenario, that returns the published payload as a byte[]
     *
     * @param publishPayload the payload stream to publish
     * @return the published byte[]
     */
    public Mono<byte[]> publishScenario(Flux<byte[]> publishPayload) {
        return publishPayload
                .transformDeferred(this::publishToMatrix)
                .transformDeferred(PublishToMatrixHandler::convertPublishResultsToByteArray)
                .single();
    }

    public Flux<String> publishScenarioWithString(Flux<byte[]> publishPayload) {
        return publishPayload
                .transformDeferred(this::publishToMatrix)
                .transformDeferred(PublishToMatrixHandler::convertPublishResultsToReadableString);
    }

    private Flux<Mqtt3PublishResult> publishToMatrix(Flux<byte[]> payloads) {
        return payloads
                .map(bytes -> {
                    log.trace("Create Publish");
                    return Mqtt3Publish.builder()
                            .topic(cfg.getMatrixPublishTopic())
                            .qos(MqttQos.AT_LEAST_ONCE)
                            .payload(ByteBuffer.wrap(bytes))
                            .build();
                })
                .publish(client::publish)
                .checkpoint("Just published to the Matrix.")
                .doOnSubscribe(subscription -> log.trace("Publishing to matrix has been subscribed"))
                .doOnError(throwable -> log.error("I tried to publish, did not work: ", throwable))
                .doOnNext(pubResult -> log.debug("Publishing acknowledged: " + new String(pubResult.getPublish().getPayloadAsBytes())));
    }

    private static Mono<byte[]> convertPublishResultsToByteArray(Flux<Mqtt3PublishResult> pubResults) {
        return pubResults
                .map(pub -> pub.getPublish().getPayloadAsBytes())
                .collect(ByteArrayOutputStream::new,
                        ByteArrayOutputStream::writeBytes)
                .map(ByteArrayOutputStream::toByteArray);
    }


    private static Flux<String> convertPublishResultsToReadableString(Flux<Mqtt3PublishResult> pubResults) {
        Flux<Integer> sequenceFlux = Flux.fromStream(IntStream.iterate(0, i -> i + 1).boxed());

        return pubResults
                .map(pub -> pub.getPublish().getPayloadAsBytes())
                .zipWith(sequenceFlux,
                        (bytes, step) -> String.format("Message %06d: %s %s",
                                step,
                                BaseEncoding.base16().upperCase().withSeparator(" ", 2).encode(bytes),
                                new String(bytes)));
    }
}
