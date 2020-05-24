package ch.phildev.springphawtrix.service;

import java.awt.*;
import java.util.Arrays;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class ColorHandlerImpl implements ColorHandler {

    private final ByteHandler byteHandler;

    public ColorHandlerImpl(ByteHandler byteHandler) {
        this.byteHandler = byteHandler;
    }

    @Override
    public Color colorFromString(List<String> rawColors) {
        if (rawColors.size() != 3) {
            throw new IllegalArgumentException("Must be exact three color values in order RGB");
        }

        return new Color(Integer.parseInt(rawColors.get(0)),
                Integer.parseInt(rawColors.get(1)),
                Integer.parseInt(rawColors.get(2)));
    }

    @Override
    public byte[] getRawColorAsPayloadArray(String rawColors) {
        Color c = colorFromString(Arrays.asList(rawColors.split(",")));
        return colorToArray(c);
    }

    @Override
    public byte[] getHexColorAsPayloadArray(String hexColor) {
        Color c = Color.decode(hexColor);
        return colorToArray(c);
    }


    @Override
    public byte[] colorToArray(Color c) {
        log.trace("Decoded Color: " + c.toString());

        byte[] colorArray = new byte[3];
        colorArray[0] = byteHandler.intToByte(c.getRed());
        colorArray[1] = byteHandler.intToByte(c.getGreen());
        colorArray[2] = byteHandler.intToByte(c.getBlue());
        return colorArray;
    }
}
