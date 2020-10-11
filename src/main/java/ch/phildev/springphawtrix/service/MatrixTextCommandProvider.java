package ch.phildev.springphawtrix.service;

import ch.phildev.springphawtrix.web.rest.dto.DrawTextDto;

public interface MatrixTextCommandProvider {

    /**
     * Convert the input of the DTO into a text frame for the matrix.
     * The DTO defines the coordinates, the color and the text to display
     *
     * @param drawTextDto
     * @return a byte buffer representation of the payload to be delivered to the matrix to display text
     */
    byte[] textDeliverFrame(DrawTextDto drawTextDto);
}
