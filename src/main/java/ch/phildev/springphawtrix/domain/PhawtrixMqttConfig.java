package ch.phildev.springphawtrix.domain;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Data
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

}
