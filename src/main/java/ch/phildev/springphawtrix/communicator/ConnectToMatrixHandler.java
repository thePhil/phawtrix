package ch.phildev.springphawtrix.communicator;

import com.hivemq.client.mqtt.mqtt3.reactor.Mqtt3ReactorClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

import ch.phildev.springphawtrix.domain.PhawtrixMqttConfig;

@Component
@Slf4j
public class ConnectToMatrixHandler {


    private final PhawtrixMqttConfig cfg;

    private final Mqtt3ReactorClient client;
    private final SubscribeToMatrixHandler subscribeToMatrixHandler;

    public ConnectToMatrixHandler(PhawtrixMqttConfig cfg, Mqtt3ReactorClient client, SubscribeToMatrixHandler subscribeToMatrixHandler) {
        this.cfg = cfg;
        this.client = client;
        this.subscribeToMatrixHandler = subscribeToMatrixHandler;
    }

    public Mono<Void> connectScenario() {

        if (client.getState().isConnectedOrReconnect()) {
            log.debug("Returned empty Mono on connect try --> Still connected!");
            return Mono.empty();
        }

        // try to connect and dispatch on Success to handler
        return client.connect()
                .doOnSubscribe(disposable -> log.debug("I have been subscribed to start establishing a connection"))
                .doOnNext(mqtt3ConnAck -> log.debug("Connected or not: " + mqtt3ConnAck.getType().name()))
                .doOnSuccess(mqtt3ConnAck -> {
                    log.debug("Successfully connected: " + mqtt3ConnAck.getType().name());
                    subscribeToMatrixHandler.goodConnected(mqtt3ConnAck);
                })
                .retry(cfg.getRetryTimes())
                .doOnError(throwable -> log.error("Could not connect to Broker: " + cfg.getBrokerHost() + " \n" +
                                                  "with error message: " + throwable.getMessage(), throwable))
                .checkpoint("Connection Scenario")
                .then();
    }
}
