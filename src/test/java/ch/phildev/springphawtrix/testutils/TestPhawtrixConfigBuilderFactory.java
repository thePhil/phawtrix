package ch.phildev.springphawtrix.testutils;

import ch.phildev.springphawtrix.domain.PhawtrixMqttConfig;

public class TestPhawtrixConfigBuilderFactory {

    public static PhawtrixMqttConfig.PhawtrixMqttConfigBuilder aDefaultConfig() {
        return PhawtrixMqttConfig.builder()
                .brokerHost("phils-home-pi.fritz.box")
                .identifier("phawtrix-server")
                .matrixPublishTopic("awtrixmatrix/a")
                .matrixSubscribeTopic("matrixClient")
                .retryTimes(5)
                .timeoutMs(1000);
    }
}
