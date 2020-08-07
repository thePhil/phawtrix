package ch.phildev.springphawtrix.app.management;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.PropertyMapper;

@NoArgsConstructor(force = true,access = AccessLevel.PRIVATE)
public final class PhawtrixAppPropertiesAppRegistrationAdapter {

    public static Map<String, AppRegistration> getAppRegistrations(PhawtrixAppProperties properties) {
        return properties.getRegistration().entrySet().stream()
                .map(appEntry -> getAppRegistration(appEntry.getKey(), appEntry.getValue()))
                .collect(Collectors.toMap(
                        AppRegistration::getAppName,
                        Function.identity()));

    }

    private static AppRegistration getAppRegistration(String appId, PhawtrixAppProperties.Registration registration){
        AppRegistration.AppRegistrationBuilder appBuilder = AppRegistration.builder().appName(appId);

        PropertyMapper  mapper = PropertyMapper.get().alwaysApplyingWhenNonNull();
        mapper.from(registration::getAppId).to(appBuilder::appName);
        mapper.from(registration::getAuthorName).to(appBuilder::appAuthor);
        mapper.from(registration::getVersion).to(appBuilder::version);

        //TODO: Arguments

        return appBuilder.build();
    }

}
