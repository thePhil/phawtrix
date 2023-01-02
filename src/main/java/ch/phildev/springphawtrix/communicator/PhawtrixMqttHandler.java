package ch.phildev.springphawtrix.communicator;

import jakarta.validation.constraints.NotNull;
import java.util.UUID;

import com.hivemq.client.mqtt.MqttClient;
import com.hivemq.client.mqtt.mqtt3.Mqtt3Client;
import com.hivemq.client.mqtt.mqtt3.reactor.Mqtt3ReactorClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

import ch.phildev.springphawtrix.domain.PhawtrixMqttConfig;

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

    @Bean
    public @NotNull Mqtt3ReactorClient createInstance() {
        return client;
    }

}
