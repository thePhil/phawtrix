package ch.phildev.springphawtrix.service;

import java.io.IOException;

public interface BmpHandler {
    byte[] getBmpAsColorMapPayload(String base64Bitmap) throws IOException;
}
