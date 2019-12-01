package ch.phildev.springphawtrix.mqtt3.reactorclient;

import com.hivemq.client.internal.mqtt.message.connect.MqttConnect;
import com.hivemq.client.internal.mqtt.message.connect.mqtt3.Mqtt3ConnectView;
import com.hivemq.client.mqtt.MqttGlobalPublishFilter;
import com.hivemq.client.mqtt.mqtt3.Mqtt3Client;
import com.hivemq.client.mqtt.mqtt3.message.connect.Mqtt3Connect;
import com.hivemq.client.mqtt.mqtt3.message.connect.connack.Mqtt3ConnAck;
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3Publish;
import com.hivemq.client.mqtt.mqtt3.message.publish.Mqtt3PublishResult;
import com.hivemq.client.mqtt.mqtt3.message.subscribe.Mqtt3Subscribe;
import com.hivemq.client.mqtt.mqtt3.message.subscribe.suback.Mqtt3SubAck;
import com.hivemq.client.mqtt.mqtt3.message.unsubscribe.Mqtt3Unsubscribe;
import com.hivemq.client.mqtt.mqtt3.message.unsubscribe.unsuback.Mqtt3UnsubAck;
import org.reactivestreams.Publisher;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.constraints.NotNull;


public interface Mqtt3ReactorClient extends Mqtt3Client {

    static @NotNull Mqtt3ReactorClient from(final @NotNull Mqtt3Client client) {
        return new MqttReactorClient(client.toRx());
    }

    default @NotNull Mono<Mqtt3ConnAck> connect() {
        return connect(Mqtt3ConnectView.of(MqttConnect.DEFAULT));
    }

    @NotNull Mono<Mqtt3ConnAck> connect(@NotNull Mqtt3Connect connect);


    @NotNull Mono<Mqtt3SubAck> subscribe(@NotNull Mqtt3Subscribe subscribe);


    @NotNull Flux<Mqtt3Publish> subscribeStream(@NotNull Mqtt3Subscribe subscribe);

    @NotNull Flux<Mqtt3Publish> publishes(@NotNull MqttGlobalPublishFilter filter);

    @NotNull Mono<Mqtt3UnsubAck> unsubscribe(@NotNull Mqtt3Unsubscribe unsubscribe);

    @NotNull Flux<Mqtt3PublishResult> publish(@NotNull Publisher<Mqtt3Publish> publishFlowable);

}
