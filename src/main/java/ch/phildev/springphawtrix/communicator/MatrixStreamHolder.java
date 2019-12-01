package ch.phildev.springphawtrix.communicator;

import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Flux;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

/**
 * Thread safe holder of {@link Flux} emitting the messages received from the matrix
 */
@Component
@Slf4j
public class MatrixStreamHolder {

    private AtomicReference<Flux<Mqtt3Publish>> streamHolder = new AtomicReference<>(null);

    /**
     * Return the {@link Flux} emitting the messages from the matrix.
     *
     * @return returns an empty {@link Optional} if subscription has not been successful
     */
    public Optional<Flux<Mqtt3Publish>> getStreamHolder() {
        return Optional.ofNullable(streamHolder.get());
    }

    /**
     * Only set the {@link Flux} emitting the values of the subscription from the matrix, if it has not been set yet.
     * Otherwise ignoring the operation.
     *
     * @param streamHolder the Flux emitting the received messages from the matrix.
     */
    public void setStreamHolder(Flux<Mqtt3Publish> streamHolder) {
        log.debug("Setting flux");
        this.streamHolder.compareAndSet(null, streamHolder);

        if(this.streamHolder.get() != null) {
            log.debug("Flux successfully set.");
        }
    }
}
