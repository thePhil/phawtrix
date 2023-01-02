package ch.phildev.springphawtrix.web.rest.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;

import lombok.Data;

@Data
public class BrightnessDto {
    @Max(255)
    @Min(10)
    private int brightness;
}
