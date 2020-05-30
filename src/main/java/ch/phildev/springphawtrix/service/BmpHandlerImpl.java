package ch.phildev.springphawtrix.service;


import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Base64;

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


        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                buf.put(colorHandler.colorTo16BitArray(new Color(img.getRGB(x, y))));
            }
        }

        return buf.array();
    }

    @Override
    public String colorArrayAsBase64Bitmap(Color[] colors) throws IOException {
        BufferedImage img = new BufferedImage(32, 8, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < img.getHeight(); y++) {
            for (int x = 0; x < img.getWidth(); x++) {
                img.setRGB(x, y, colors[y * 32 + x].getRGB());
            }
        }

        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        ImageIO.write(img, "BMP", new File("out.bmp"));
        ImageIO.write(img, "BMP", bos);
        return Base64.getEncoder().encodeToString(bos.toByteArray());
    }
}
