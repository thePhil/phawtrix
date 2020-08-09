package ch.phildev.springphawtrix.app.domain;

import java.util.Map;

import lombok.AccessLevel;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

@NoArgsConstructor(force = true, access = AccessLevel.PRIVATE)
@Getter
@Builder
@ToString
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public final class AppRegistration {

    @EqualsAndHashCode.Include
    private String appName;
    private String appAuthor;
    private String version;
    private Map<String, String> arguments;

}
