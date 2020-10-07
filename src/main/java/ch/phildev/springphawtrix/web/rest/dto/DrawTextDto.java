package ch.phildev.springphawtrix.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import ch.phildev.springphawtrix.domain.Coordinates;

@Value
@Builder(toBuilder = true)
@RequiredArgsConstructor
@NonNull
@JsonDeserialize(builder = DrawTextDto.DrawTextDtoBuilder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrawTextDto {

    private final Coordinates coordinates;
    private final String text;
    private final String hexTextColor;

    @JsonPOJOBuilder(withPrefix = "")
    public static class DrawTextDtoBuilder {
        Coordinates coordinatesWithBuilder(Coordinates.CoordinatesBuilder coordinatesBuilder) {
            return coordinatesBuilder.build();
        }
    }
}
