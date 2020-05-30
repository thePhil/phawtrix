package ch.phildev.springphawtrix.service;

import java.awt.*;
import java.io.IOException;

public interface BmpHandler {
    byte[] getBmpAsColorMapPayload(String base64Bitmap) throws IOException;

    String colorArrayAsBase64Bitmap(Color[] colors) throws IOException;
}
