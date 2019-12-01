package ch.phildev.springphawtrix.web.rest.dto;

import lombok.Data;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;

@Data
public class BrightnessDto {
    @Max(255)
    @Min(10)
    private int brightness;
}
