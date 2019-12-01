package ch.phildev.springphawtrix.web.rest.dto;

import lombok.Data;
import lombok.NonNull;

@Data(staticConstructor = "of")
public class AnswerDto {
    @NonNull
    private String payLoadToMatrix;
}
