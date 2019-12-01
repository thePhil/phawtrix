package ch.phildev.springphawtrix.communicator;

import ch.phildev.springphawtrix.domain.PhawtrixMqttConfig;
import ch.phildev.springphawtrix.mqtt3.reactorclient.Mqtt3ReactorClient;
import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt3.Mqtt3Client;
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish;
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3PublishResult;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;
import java.io.ByteArrayOutputStream;
import java.nio.ByteBuffer;
import java.util.UUID;

@Component
@Slf4j
public class PhawtrixMqttHandler {

    private final PhawtrixMqttConfig cfg;

    private final @NotNull Mqtt3ReactorClient client;


    public PhawtrixMqttHandler(PhawtrixMqttConfig cfg) {
        this.cfg = cfg;

        client = Mqtt3ReactorClient.from(init());
    }

    private Mqtt3Client init() {
        log.trace("Initializing the client");
        //building the client
        return MqttClient.builder()
                .useMqttVersion3()
                .identifier(cfg.getIdentifier() + UUID.randomUUID().toString())
                .serverHost(cfg.getBrokerHost())
                .automaticReconnectWithDefaultConfig()
                .build();
    }

    public Flux<Mqtt3PublishResult> publishToMatrix(Flux<byte[]> payloads) {
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

    @Bean
    public @NotNull Mqtt3ReactorClient createInstance() {
        return client;
    }

    public static Mono<byte[]> convertPublishResultsToByteArray(Flux<Mqtt3PublishResult> pubResults) {
        return pubResults
                .map(pub -> pub.getPublish().getPayloadAsBytes())
                .collect(ByteArrayOutputStream::new,
                        ByteArrayOutputStream::writeBytes)
                .map(ByteArrayOutputStream::toByteArray);
    }

}
