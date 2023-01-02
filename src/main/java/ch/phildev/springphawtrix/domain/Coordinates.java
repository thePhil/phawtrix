package ch.phildev.springphawtrix.domain;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import lombok.Builder;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import lombok.Value;

import java.beans.Transient;

@Builder(toBuilder = true)
@Value(staticConstructor = "of")
@RequiredArgsConstructor
@NonNull
@JsonDeserialize(builder = Coordinates.CoordinatesBuilder.class)
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Coordinates {

    @Min(0)
    @Max(31) int x;

    @Min(0)
    @Max(7) int y;


    /**
     * Expect coordinates in the string "x,y" as numbers
     *
     * @param coordinates x,y coordinates
     * @return parsed as object
     */
    static Coordinates of(String coordinates) {

        String[] cords = coordinates.split(",");
        if (cords.length != 2) {
            throw new IllegalArgumentException("A coordinate must be expressed as String separated by a ','");
        }

        return Coordinates.builder()
                .x(Integer.parseInt(cords[0]))
                .y(Integer.parseInt(cords[1]))
                .build();
    }

    @Transient
    public byte getByteX() {
        return (byte) x;
    }

    @Transient
    public byte getByteY() {
        return (byte) y;
    }
}
