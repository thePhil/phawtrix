package ch.phildev.springphawtrix.service;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;

import com.google.common.io.BaseEncoding;
import org.springframework.stereotype.Component;

@Component
public class BmpHandlerImpl implements BmpHandler {

    private final ColorHandler colorHandler;

    public BmpHandlerImpl(ColorHandler colorHandler) {
        this.colorHandler = colorHandler;
    }


    @Override
    public byte[] getBmpAsColorMapPayload(String base64Bitmap) throws IOException {

        byte[] decodedBmp = BaseEncoding.base64().decode(base64Bitmap);


        BufferedImage img = ImageIO.read(new ByteArrayInputStream(decodedBmp));
        ByteBuffer buf = ByteBuffer.allocate((img.getHeight() * img.getWidth()) * 3);


        for (int row = 0; row < img.getHeight(); row++) {
            for (int column = 0; column < img.getWidth(); column++) {
                buf.put(colorHandler.colorToArray(new Color(img.getRGB(row, column))));
            }
        }

        return buf.array();
    }
}
