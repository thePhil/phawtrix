package ch.phildev.springphawtrix.app.management;

import ch.phildev.springphawtrix.app.domain.AppRegistration;
import ch.phildev.springphawtrix.app.management.config.PhawtrixAppProperties;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.PropertyMapper;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@NoArgsConstructor(force = true,access = AccessLevel.PRIVATE)
public final class PhawtrixAppPropertiesAppRegistrationAdapter {

    public static Map<String, AppRegistration> getAppRegistrations(PhawtrixAppProperties properties) {
        return properties.getRegistration()
                .entrySet().stream()
                .map(appEntry -> getAppRegistration(appEntry.getKey(), appEntry.getValue()))
                .collect(Collectors.toMap(
                        AppRegistration::getAppName,
                        Function.identity()));

    }

    private static AppRegistration getAppRegistration(String appId, PhawtrixAppProperties.Registration registration){
        AppRegistration.AppRegistrationBuilder appRegistrationBuilder = AppRegistration.builder().appName(appId);

        PropertyMapper  mapper = PropertyMapper.get().alwaysApplyingWhenNonNull();
        mapper.from(registration::getAppId).to(appRegistrationBuilder::appName);
        mapper.from(registration::getAuthorName).to(appRegistrationBuilder::appAuthor);
        mapper.from(registration::getVersion).to(appRegistrationBuilder::version);
        mapper.from(registration::getMilliInterval).to(appRegistrationBuilder::milliInterval);

        //TODO: Arguments

        return appRegistrationBuilder.build();
    }

}
