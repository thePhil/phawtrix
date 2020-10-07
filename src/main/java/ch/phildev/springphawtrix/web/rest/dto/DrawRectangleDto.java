package ch.phildev.springphawtrix.web.rest.dto;

import javax.annotation.Nonnegative;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import ch.phildev.springphawtrix.domain.Coordinates;

@Value(staticConstructor = "of")
@Builder
@NonNull
@JsonDeserialize(builder = DrawRectangleDto.DrawRectangleDtoBuilder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrawRectangleDto {

    private final Coordinates coordinates;
    @Nonnegative
    private final int width;
    @Nonnegative
    private final int height;
    private final String hexTextColor;

    public static class DrawRectangleDtoBuilder{

    }
}
