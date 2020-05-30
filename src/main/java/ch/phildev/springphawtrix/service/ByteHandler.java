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

    /**
     * Converts an integer of [0;65535} into an unsigned short representation as array of two elements
     *
     * @param theInt to convert
     * @return the unsigned byte array representation
     */
    byte[] intToShortByteArray(int theInt);

    /**
     * Receive a byte array and turn it into a base16 encoded String (Hex String)
     *
     * @return the encoded String
     */
    String base16ByteArrayEncode(byte[] byteArray);
}
