package ch.phildev.springphawtrix.service;

import ch.phildev.springphawtrix.domain.Coordinates;

public interface CoordinateDecoder {

    byte[] getPayloadFromCoordinates(Coordinates coordinates);
}
