package ch.phildev.springphawtrix.mqtt3.reactorclient;

import com.hivemq.client.mqtt.MqttGlobalPublishFilter;
import com.hivemq.client.mqtt.mqtt3.Mqtt3AsyncClient;
import com.hivemq.client.mqtt.mqtt3.Mqtt3BlockingClient;
import com.hivemq.client.mqtt.mqtt3.Mqtt3ClientConfig;
import com.hivemq.client.mqtt.mqtt3.Mqtt3RxClient;
import com.hivemq.client.mqtt.mqtt3.message.connect.Mqtt3Connect;
import com.hivemq.client.mqtt.mqtt3.message.connect.connack.Mqtt3ConnAck;
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish;
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3PublishResult;
import com.hivemq.client.mqtt.mqtt3.message.subscribe.Mqtt3Subscribe;
import com.hivemq.client.mqtt.mqtt3.message.subscribe.suback.Mqtt3SubAck;
import com.hivemq.client.mqtt.mqtt3.message.unsubscribe.Mqtt3Unsubscribe;
import com.hivemq.client.mqtt.mqtt3.message.unsubscribe.unsuback.Mqtt3UnsubAck;
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;


public class MqttReactorClient implements Mqtt3ReactorClient {

    private final @NotNull Mqtt3RxClient delegate;

    public MqttReactorClient(final @NotNull Mqtt3RxClient delegate) {
        this.delegate = delegate;
    }

    @Override
    public @NotNull Mono<Mqtt3ConnAck> connect() {
        return Mono.fromDirect(delegate.connect().toFlowable());
    }

    @Override
    public @NotNull Mono<Mqtt3ConnAck> connect(@NotNull Mqtt3Connect connect) {
        return Mono.fromDirect(delegate.connect(connect).toFlowable());
    }

    @Override
    public @NotNull Mono<Mqtt3SubAck> subscribe(@NotNull Mqtt3Subscribe subscribe) {
        return Mono.fromDirect(delegate.subscribe(subscribe).toFlowable());
    }

    @Override
    public @NotNull Flux<Mqtt3Publish> subscribeStream(@NotNull Mqtt3Subscribe subscribe) {
        return Flux.from(delegate.subscribeStream(subscribe));
    }

    @Override
    public @NotNull Flux<Mqtt3Publish> publishes(@NotNull MqttGlobalPublishFilter filter) {
        return Flux.from(delegate.publishes(filter));
    }

    @Override
    public @NotNull Mono<Mqtt3UnsubAck> unsubscribe(@NotNull Mqtt3Unsubscribe unsubscribe) {
        return Mono.fromDirect(delegate.unsubscribe(unsubscribe).toFlowable());
    }

    @Override
    public @NotNull Flux<Mqtt3PublishResult> publish(@NotNull Publisher<Mqtt3Publish> publishFlowable) {
        return Flux.from(delegate.publish(Flowable.fromPublisher(publishFlowable)));
    }

    @Override
    public @NotNull
    Mqtt3ClientConfig getConfig() {
        return delegate.getConfig();
    }

    @Override
    public @NotNull
    Mqtt3RxClient toRx() {
        return delegate;
    }

    @Override
    public @NotNull
    Mqtt3AsyncClient toAsync() {
        return delegate.toAsync();
    }

    @Override
    public @NotNull
    Mqtt3BlockingClient toBlocking() {
        return delegate.toBlocking();
    }
}
