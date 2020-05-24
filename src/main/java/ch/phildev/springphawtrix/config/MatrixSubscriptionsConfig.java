package ch.phildev.springphawtrix.config;

import com.hivemq.client.mqtt.datatypes.MqttQos;
import com.hivemq.client.mqtt.mqtt3.message.subscribe.Mqtt3Subscribe;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import ch.phildev.springphawtrix.domain.PhawtrixMqttConfig;

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
