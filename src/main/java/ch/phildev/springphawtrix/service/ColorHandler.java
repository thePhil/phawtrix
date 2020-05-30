package ch.phildev.springphawtrix.service;

import java.awt.*;
import java.util.List;

import org.springframework.stereotype.Component;

@Component
public interface ColorHandler {

    /**
     * Takes three RGB values as List of Strings and returns a {@link Color}
     *
     * @param rawColors the colors in the order R, G, B
     * @return a Color
     */
    Color colorFromString(List<String> rawColors);

    /**
     * This function takes a rawColors string in the format "<valueRed>,<valueGreen>, <valueBlue>", so white
     * would be "255,255,255"
     *
     * @param rawColors string as r,g,b separated by a ,
     * @return the corresponding payload
     */
    byte[] getRawColorAsPayloadArray(String rawColors);

    /**
     * Get Payload colors from Hex encoded color string (#FFFFFF stands for White)
     * @param hexColor the color hex string
     * @return the corresponding payload
     */
    byte[] getHexColorAsPayloadArray(String hexColor);

    /**
     * Get Payload color from {@link Color}
     * @param c the color
     * @return the corresponding payload
     */
    byte[] colorToArray(Color c);


    /**
     * Convert a given color to a Neomatrix compatible 16 bit representation
     * @param c
     * @return
     */
    byte[] colorTo16BitArray(Color c);
}
