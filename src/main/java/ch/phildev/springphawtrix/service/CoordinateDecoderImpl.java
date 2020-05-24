package ch.phildev.springphawtrix.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import ch.phildev.springphawtrix.domain.Coordinates;

@Component
@Slf4j
public class CoordinateDecoderImpl implements CoordinateDecoder {

    private final ByteHandler byteHandler;

    public CoordinateDecoderImpl(ByteHandler byteHandler) {
        this.byteHandler = byteHandler;
    }


    @Override
    public byte[] getPayloadFromCoordinates(Coordinates coordinates) {
        var retArray = new byte[4];
        retArray[0] = byteHandler.intToByte(0);
        retArray[1] = coordinates.getByteX();
        retArray[2] = byteHandler.intToByte(0);
        retArray[3] = coordinates.getByteY();
        return retArray;
    }
}
