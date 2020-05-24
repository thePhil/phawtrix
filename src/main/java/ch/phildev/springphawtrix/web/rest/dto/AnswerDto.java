package ch.phildev.springphawtrix.web.rest.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@Value
@Builder
@JsonDeserialize(builder = AnswerDto.AnswerDtoBuilder.class)
public class AnswerDto {
    @NonNull
    private String payLoadToMatrix;
}
