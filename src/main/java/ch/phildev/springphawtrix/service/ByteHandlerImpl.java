package ch.phildev.springphawtrix.service;

import org.springframework.stereotype.Component;

@Component
public class ByteHandlerImpl implements ByteHandler {

    @Override
    public byte[] intToByteArray(int theInt) {
        byte[] byteArray = new byte[1];
        byteArray[0] = intToByte(theInt);
        return byteArray;
    }

    @Override
    public byte intToByte(int theInt) {
        return (byte) theInt;
    }


}
