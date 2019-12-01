package ch.phildev.springphawtrix.config;

import ch.phildev.springphawtrix.domain.PhawtrixMqttConfig;
import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt3.message.subscribe.Mqtt3Subscribe;
import com.hivemq.client.mqtt.mqtt5.message.subscribe.Mqtt5Subscribe;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MatrixSubscriptionsConfig {

    @Bean
    public Mqtt3Subscribe matrixSubscription(PhawtrixMqttConfig cfg) {
        return Mqtt3Subscribe.builder()
                .topicFilter(cfg.getMatrixSubscribeTopic())
                .qos(MqttQos.AT_LEAST_ONCE)
                .build();
    }
}
