package ch.phildev.springphawtrix.web.rest.dto;

import ch.phildev.springphawtrix.domain.Coordinates;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

@Value(staticConstructor = "of")
@Builder
@RequiredArgsConstructor
@NonNull
@JsonDeserialize(builder = DrawDto.DrawDtoBuilder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrawDto {

    private final Coordinates coordinates;
    private final String text;
    private final String hexTextColor;


    @JsonPOJOBuilder(withPrefix = "")
    public static class DrawDtoBuilder {
    }
}
