package ch.phildev.springphawtrix.service;

public interface ByteHandler {

    /**
     * Converts an integer of [0; 255] into an unsigned byte representation
     * @param theInt to convert
     * @return the unsigned byte array representation
     */
    byte[] intToByteArray(int theInt);

    /**
     * Converts an integer of [0; 255] into an unsigned byte representation
     * @param theInt to convert
     * @return the unsigned byte representation
     */
    byte intToByte(int theInt);
}
