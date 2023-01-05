package ch.phildev.springphawtrix.domain;

import lombok.Builder;
import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Builder
@Configuration
@ConfigurationProperties(prefix = "phawtrix")
public class PhawtrixMqttConfig {
    private String identifier;
    private String brokerHost;
    // should be awtrixmatrix/#
    private String matrixPublishTopic;
    // should be matrixClient
    private String matrixSubscribeTopic;
    private long retryTimes;
    private long timeoutMs;

}
