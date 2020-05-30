package ch.phildev.springphawtrix.service;

import java.awt.*;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

@Slf4j
class ColorHandlerImplTest {

    private ColorHandler colorHandler;


    @BeforeEach
    void setUp() {
        colorHandler = new ColorHandlerImpl(new ByteHandlerImpl());
    }

    @Test
    void colorTo16Bit() {
        // given
        Color c = Color.YELLOW;
        log.debug("Magenta: ({},{},{})", c.getRed() >> 3, c.getGreen() >> 3, c.getBlue() >> 3);
        log.debug("unsigned: ({},{},{})", Integer.toBinaryString(((c.getRed()) & 0xF8) << 8),
                Integer.toBinaryString(((c.getGreen()) & 0xFC) << 3),
                Integer.toBinaryString(((c.getBlue()) >> 3)));

        log.debug("{}",
                Integer.toBinaryString(((c.getRed() & 0xF8) << 8) | ((c.getGreen() & 0xFC) << 3) | c.getBlue() >> 3));

    }
}
