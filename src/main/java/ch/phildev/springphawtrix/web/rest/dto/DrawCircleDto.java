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

@Value(staticConstructor = "of")
@Builder
@RequiredArgsConstructor
@NonNull
@JsonDeserialize(builder = DrawCircleDto.DrawCircleDtoBuilder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrawCircleDto {

    private final Coordinates coordinates;
    private final String hexTextColor;
    private final int radius;
    private final boolean fillCircle;

    @JsonPOJOBuilder(withPrefix = "")
    public static class DrawCircleDtoBuilder {

    }
}
