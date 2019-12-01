package ch.phildev.springphawtrix.communicator;

import ch.phildev.springphawtrix.mqtt3.reactorclient.Mqtt3ReactorClient;
import com.hivemq.client.mqtt.mqtt3.message.connect.connack.Mqtt3ConnAck;
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish;
import com.hivemq.client.mqtt.mqtt3.message.subscribe.Mqtt3Subscribe;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;
import reactor.core.scheduler.Schedulers;

import java.nio.ByteBuffer;
import java.nio.charset.StandardCharsets;

@Component
@Slf4j
public class SubscribeToMatrixHandler {

    private final Mqtt3ReactorClient client;

    private final Mqtt3Subscribe subscription;

    private final MatrixStreamHolder streamHolder;


    public SubscribeToMatrixHandler(Mqtt3ReactorClient client, Mqtt3Subscribe subscription, MatrixStreamHolder streamHolder) {
        this.client = client;
        this.subscription = subscription;
        this.streamHolder = streamHolder;
    }

    public void goodConnected(Mqtt3ConnAck mqtt3ConnAck) {
        // TODO: create a router that decides what todo with the matrix messages

        log.debug("Now I should subscribe to the matrix channel");

        Flux<Mqtt3Publish> matrixStreamPublisher = provideMatrixReturnChannel().checkpoint("Subscribed to matrix return channel.")
                .share()
//                .cache(10)
                .doOnSubscribe(subscription1 -> log.debug("Matrix client channel has been subscribed"))
                .doOnCancel(() -> log.debug("Subscription cancelled"));

        // share the flux with other parts of the application, also a router
        streamHolder.setStreamHolder(matrixStreamPublisher);
        streamHolder.getStreamHolder().get().subscribe();
    }

    // TODO: change return type to specific Matrix Messages
    public Flux<Mqtt3Publish> provideMatrixReturnChannel() {
        return client.subscribeStream(subscription)
                .doOnNext(this::logPayload)
                .publishOn(Schedulers.single());
    }

    private void logPayload(Mqtt3Publish publish) {
        log.debug("RECEIVED PAYLOAD: " + StandardCharsets.UTF_8.decode(ByteBuffer.wrap(publish.getPayloadAsBytes())));
    }
}
