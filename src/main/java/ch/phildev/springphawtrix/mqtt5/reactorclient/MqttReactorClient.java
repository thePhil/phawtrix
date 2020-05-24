package ch.phildev.springphawtrix.mqtt5.reactorclient;

import javax.validation.constraints.NotNull;

import com.hivemq.client.mqtt.MqttGlobalPublishFilter;
import com.hivemq.client.mqtt.mqtt5.Mqtt5AsyncClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5BlockingClient;
import com.hivemq.client.mqtt.mqtt5.Mqtt5ClientConfig;
import com.hivemq.client.mqtt.mqtt5.Mqtt5RxClient;
import com.hivemq.client.mqtt.mqtt5.message.connect.Mqtt5Connect;
import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAck;
import com.hivemq.client.mqtt.mqtt5.message.disconnect.Mqtt5Disconnect;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PublishResult;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.Mqtt5Subscribe;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.suback.Mqtt5SubAck;
import com.hivemq.client.mqtt.mqtt5.message.unsubscribe.Mqtt5Unsubscribe;
import com.hivemq.client.mqtt.mqtt5.message.unsubscribe.unsuback.Mqtt5UnsubAck;
import io.reactivex.Flowable;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class MqttReactorClient implements Mqtt5ReactorClient {

    private final @NotNull Mqtt5RxClient delegate;

    public MqttReactorClient(final @NotNull Mqtt5RxClient delegate) {
        this.delegate = delegate;
    }

    @Override
    public @NotNull Mono<Mqtt5ConnAck> connect(@NotNull Mqtt5Connect connect) {
        return Mono.fromDirect(delegate.connect(connect).toFlowable());
    }

    @Override
    public @NotNull Mono<Mqtt5SubAck> subscribe(@NotNull Mqtt5Subscribe subscribe) {
        return Mono.fromDirect(delegate.subscribe(subscribe).toFlowable());
    }

    @Override
    public @NotNull Flux<Mqtt5Publish> subscribeStream(@NotNull Mqtt5Subscribe subscribe) {
        return Flux.from(delegate.subscribeStream(subscribe));
    }

    @Override
    public @NotNull Flux<Mqtt5Publish> publishes(@NotNull MqttGlobalPublishFilter filter) {
        return Flux.from(delegate.publishes(filter));
    }

    @Override
    public @NotNull Mono<Mqtt5UnsubAck> unsubscribe(@NotNull Mqtt5Unsubscribe unsubscribe) {
        return Mono.fromDirect(delegate.unsubscribe(unsubscribe).toFlowable());
    }

    @Override
    public @NotNull Flux<Mqtt5PublishResult> publish(@NotNull Publisher<Mqtt5Publish> publishFlowable) {
        return Flux.from(delegate.publish(Flowable.fromPublisher(publishFlowable)));
    }

    @Override
    public @NotNull Mono<Void> reauth() {
        return Mono.fromDirect(delegate.reauth().toFlowable());
    }

    @Override
    public @NotNull Mono<Void> disconnect(@NotNull Mqtt5Disconnect disconnect) {
        return Mono.fromDirect(delegate.disconnect(disconnect).toFlowable());
    }

    @Override
    public @NotNull
    Mqtt5ClientConfig getConfig() {
        return delegate.getConfig();
    }

    @Override
    public @NotNull
    Mqtt5RxClient toRx() {
        return delegate;
    }

    @Override
    public @NotNull
    Mqtt5AsyncClient toAsync() {
        return delegate.toAsync();
    }

    @Override
    public @NotNull
    Mqtt5BlockingClient toBlocking() {
        return delegate.toBlocking();
    }
}
