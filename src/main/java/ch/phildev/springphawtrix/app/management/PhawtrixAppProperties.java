package ch.phildev.springphawtrix.app.management;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.Map;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.util.StringUtils;

@ConfigurationProperties(prefix = "phawtrix.app")
@Getter
public class PhawtrixAppProperties {
    private final Map<String, Registration> registration = new HashMap<>();

    @PostConstruct
    public void validate(){
        getRegistration().values().forEach(this::validateAppRegistration);
    }

    private void validateAppRegistration(Registration registration) {
        //TODO implement fancy validation here

        if (!StringUtils.hasText(registration.getAuthorName())) {
            throw new IllegalStateException("An authors name must be provided");
        }
    }

    @Getter
    @Setter
    public static class Registration {
        private String appId;
        private String authorName;
        private String version;
    }
}
