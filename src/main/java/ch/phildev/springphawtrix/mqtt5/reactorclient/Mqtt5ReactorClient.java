package ch.phildev.springphawtrix.mqtt5.reactorclient;

import com.hivemq.client.internal.mqtt.message.connect.MqttConnect;
import com.hivemq.client.internal.mqtt.message.connect.MqttConnectBuilder;
import com.hivemq.client.internal.mqtt.message.disconnect.MqttDisconnect;
import com.hivemq.client.internal.mqtt.message.disconnect.MqttDisconnectBuilder;
import com.hivemq.client.internal.mqtt.message.subscribe.MqttSubscribeBuilder;
import com.hivemq.client.internal.mqtt.message.unsubscribe.MqttUnsubscribeBuilder;
import com.hivemq.client.mqtt.MqttGlobalPublishFilter;
import com.hivemq.client.mqtt.mqtt5.Mqtt5Client;
import com.hivemq.client.mqtt.mqtt5.message.connect.Mqtt5Connect;
import com.hivemq.client.mqtt.mqtt5.message.connect.Mqtt5ConnectBuilder;
import com.hivemq.client.mqtt.mqtt5.message.connect.connack.Mqtt5ConnAck;
import com.hivemq.client.mqtt.mqtt5.message.disconnect.Mqtt5Disconnect;
import com.hivemq.client.mqtt.mqtt5.message.disconnect.Mqtt5DisconnectBuilder;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5Publish;
import com.hivemq.client.mqtt.mqtt5.message.publish.Mqtt5PublishResult;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.Mqtt5Subscribe;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.Mqtt5SubscribeBuilder;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.suback.Mqtt5SubAck;
import com.hivemq.client.mqtt.mqtt5.message.unsubscribe.Mqtt5Unsubscribe;
import com.hivemq.client.mqtt.mqtt5.message.unsubscribe.Mqtt5UnsubscribeBuilder;
import com.hivemq.client.mqtt.mqtt5.message.unsubscribe.unsuback.Mqtt5UnsubAck;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;

public interface Mqtt5ReactorClient extends Mqtt5Client {

    static @NotNull Mqtt5ReactorClient from(final @NotNull Mqtt5Client client) {
        return new MqttReactorClient(client.toRx());
    }

    default @NotNull Mono<Mqtt5ConnAck> connect() {
        return connect(MqttConnect.DEFAULT);
    }

    @NotNull Mono<Mqtt5ConnAck> connect(@NotNull Mqtt5Connect connect);

    default @NotNull Mqtt5ConnectBuilder.Nested<Mono<Mqtt5ConnAck>> connectWith() {
        return new MqttConnectBuilder.Nested<>(this::connect);
    }

    @NotNull Mono<Mqtt5SubAck> subscribe(@NotNull Mqtt5Subscribe subscribe);

    default @NotNull Mqtt5SubscribeBuilder.Nested.Start<Mono<Mqtt5SubAck>> subscribeWith() {
        return new MqttSubscribeBuilder.Nested<>(this::subscribe);
    }

    @NotNull Flux<Mqtt5Publish> subscribeStream(@NotNull Mqtt5Subscribe subscribe);

    default @NotNull Mqtt5SubscribeBuilder.Nested.Start<Flux<Mqtt5Publish>> subscribeStreamWith() {
        return new MqttSubscribeBuilder.Nested<>(this::subscribeStream);
    }

    @NotNull Flux<Mqtt5Publish> publishes(@NotNull MqttGlobalPublishFilter filter);

    @NotNull Mono<Mqtt5UnsubAck> unsubscribe(@NotNull Mqtt5Unsubscribe unsubscribe);

    default @NotNull Mqtt5UnsubscribeBuilder.Nested.Start<Mono<Mqtt5UnsubAck>> unsubscribeWith() {
        return new MqttUnsubscribeBuilder.Nested<>(this::unsubscribe);
    }

    @NotNull Flux<Mqtt5PublishResult> publish(@NotNull Publisher<Mqtt5Publish> publishFlowable);

    @NotNull Mono<Void> reauth();

    default @NotNull Mono<Void> disconnect() {
        return disconnect(MqttDisconnect.DEFAULT);
    }

    @NotNull Mono<Void> disconnect(@NotNull Mqtt5Disconnect disconnect);

    default @NotNull Mqtt5DisconnectBuilder.Nested<Mono<Void>> disconnectWith() {
        return new MqttDisconnectBuilder.Nested<>(this::disconnect);
    }
}
