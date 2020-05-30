package ch.phildev.springphawtrix.service;

import com.google.common.io.BaseEncoding;
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

    @Override
    public byte[] intToShortByteArray(int theInt) {
        byte[] shortBytes = new byte[2];
        shortBytes[0] = (byte) (theInt >> 8);
        shortBytes[1] = (byte) ((theInt << 24) >> 24);
        return shortBytes;
    }

    @Override
    public String base16ByteArrayEncode(byte[] byteArray) {
        return BaseEncoding.base16().encode(byteArray);
    }
}
