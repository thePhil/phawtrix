package ch.phildev.springphawtrix.service;

import ch.phildev.springphawtrix.domain.PhawtrixCommand;
import com.google.common.primitives.Bytes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class CommandEncoderImpl implements CommandEncoder {

    @Override
    public byte[] getPayloadForMatrix(PhawtrixCommand command, byte[]... params) {
        byte[] payload = new byte[0];

        byte[] cmdArray = {(byte) command.getByteValue()};

        log.trace("Command length: " + cmdArray.length);
        log.trace("params length: " + cmdArray.length);

        switch (command) {
            case SHOW:
            case CLEAR:
            case RESET:
            case GET_MATRIX_INFO:
            case RESET_WIFI:
            case PING:
            case PLAY_MP3:
                payload = cmdArray;
                break;

            case DRAW_TEXT:
            case DRAW_BMP:
            case DRAW_CIRCLE:
            case FILL_CIRCLE:
            case DRAW_PIXEL:
            case DRAW_RECT:
//                payload = handleCommandWithCoordinates(command, splittedPayloads);
                break;

            case SET_BRIGHTNESS:
                payload = Bytes.concat(cmdArray, params[0]);
                break;
            case FILL_MATRIX:
                payload = Bytes.concat(cmdArray, params[0]);
                break;
            case DRAW_LINE:
//                payload = Bytes.concat(cmdArray,
//                        getCoordinatesArray(splittedPayloads.get(1)),
//                        getCoordinatesArray(splittedPayloads.get(2)),
//                        getColorAsArray(splittedPayloads.get(3)));

                break;
            default:
                throw new IllegalStateException("Command not implemented: " + command);
        }

        return payload;
    }
}
