package ch.phildev.springphawtrix.domain;

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

import javax.validation.constraints.NotNull;
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
        log.error("Initializing the client.");
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
                .checkpoint()
                .log()
                .doOnNext(bytes -> log.debug("Now converting"))
                .map(bytes -> {
                    log.debug("Create Publish");
                    return Mqtt3Publish.builder()
                            .topic(cfg.getMatrixPublishTopic())
                            .qos(MqttQos.AT_LEAST_ONCE)
                            .payload(ByteBuffer.wrap(bytes))
                            .build();
                })
                .publish(client::publish)
                .doOnSubscribe(subscription -> log.debug("I have been subscribed"))
                .doOnError(throwable -> log.debug("I tried to connect"))
                .doOnNext(pubResult -> log.debug("Publishing acknowledged: " + new String(pubResult.getPublish().getPayloadAsBytes())));
    }

    @Bean
    public @NotNull Mqtt3ReactorClient createInstance() {
        return client;
    }
}
