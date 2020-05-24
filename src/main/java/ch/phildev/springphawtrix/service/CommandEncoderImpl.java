package ch.phildev.springphawtrix.service;

import com.google.common.primitives.Bytes;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import ch.phildev.springphawtrix.domain.PhawtrixCommand;

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
                // params: coordinates, color, text
            case DRAW_CIRCLE:
            case FILL_CIRCLE:
                // params: coordinates, radius, color
            case DRAW_LINE:
                // params: coordinates1, coordinates2, color
                payload = Bytes.concat(cmdArray, params[0], params[1], params[2]);
                break;
            case DRAW_PIXEL:
                payload = Bytes.concat(cmdArray, params[0], params[1]);
                break;
            case DRAW_BMP:
                // params: coordinates, width, height, bmp
            case DRAW_RECT:
                // params: coordinates, width, height, color
                payload = Bytes.concat(cmdArray, params[0], params[1], params[2], params[3]);
                break;

            case SET_BRIGHTNESS:
            case FILL_MATRIX:
                payload = Bytes.concat(cmdArray, params[0]);
                break;
            default:
                throw new IllegalStateException("Command not implemented: " + command);
        }

        return payload;
    }
}
