package ch.phildev.springphawtrix.service;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

@Slf4j
class ByteHandlerImplTest {

    @Test
    void intToShortByteArray() {
        int shortByte = 254;

        byte[] convertedBytes = new ByteHandlerImpl().intToShortByteArray(shortByte);
        log.debug("{}", Integer.toBinaryString(shortByte));
        log.debug("{} {}", Integer.toBinaryString(Byte.toUnsignedInt(convertedBytes[0])),
                Integer.toBinaryString(Byte.toUnsignedInt(convertedBytes[1])));

    }
}
