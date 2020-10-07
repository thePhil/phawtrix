package ch.phildev.springphawtrix.web.rest.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

import ch.phildev.springphawtrix.domain.Coordinates;

@Value(staticConstructor = "of")
@Builder
@NonNull
@JsonDeserialize(builder = DrawBmpDto.DrawBmpDtoBuilder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DrawBmpDto {

   private final Coordinates coordinates;
   private final int width;
   private final int height;
   // Base64 encoded byte array of colors. Where colors are encoded line by line top to bottom and each line left to
   // right
   // Here we expect an bitmap
   private final String base64Bitmap;

   @JsonPOJOBuilder(withPrefix = "")
   public static class DrawBmpDtoBuilder{

   }
}
