package ch.phildev.springphawtrix.domain;

import ch.phildev.springphawtrix.mqtt3.reactorclient.Mqtt3ReactorClient;
import com.hivemq.client.mqtt.mqtt3.message.connect.connack.Mqtt3ConnAck;
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish;
import com.hivemq.client.mqtt.mqtt3.message.subscribe.Mqtt3Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class SubscribeToMatrixHandler {

    private final Mqtt3ReactorClient client;

    private final Mqtt3Subscribe subscription;


    public SubscribeToMatrixHandler(Mqtt3ReactorClient client, Mqtt3Subscribe subscription) {
        this.client = client;
        this.subscription = subscription;
    }

    public void goodConnected(Mqtt3ConnAck mqtt3ConnAck) {
        // react on Connection with specific behaviour (logging only)
        // TODO: create a router that decides what todo with the matrix messages
//        provideMatrixReturnChannel().log().ignoreElements().subscribe();

        log.debug("Now I should subscribe to a channel");
    }

    // TODO: change return type to specific Matrix Messages
    public Flux<Mqtt3Publish> provideMatrixReturnChannel() {
        return client.subscribeStream(subscription)
                .doOnNext(mqtt5Publish -> mqtt5Publish.getPayload().ifPresent(byteBuffer -> log.debug("Received " +
                        "Payload: " + StandardCharsets.UTF_8.decode(byteBuffer))))
                .publish();
    }
}
