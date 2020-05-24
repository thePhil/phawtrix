package ch.phildev.springphawtrix.testutils;

import java.awt.*;

import ch.phildev.springphawtrix.domain.Coordinates;
import ch.phildev.springphawtrix.web.rest.dto.DrawTextDto;

public class TestDataBuilderFactory {

    public static DrawTextDto.DrawTextDtoBuilder aDefaultDrawText() {
        return DrawTextDto.builder()
                .coordinates(Coordinates.builder()
                        .x(1).y(1).build())
                .text("Hello P")
                .hexTextColor(String.format("#%06X", 0xFFFFFF & Color.BLUE.getRGB()));
    }
}
