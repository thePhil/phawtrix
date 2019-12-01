package ch.phildev.springphawtrix.service;

import ch.phildev.springphawtrix.domain.PhawtrixCommand;

public interface CommandEncoder {
//    Mono<byte[]> commandToMatrix(PhawtrixCommand command, byte[]... params);

    byte[] getPayloadForMatrix(PhawtrixCommand clear, byte[]... params);
}
