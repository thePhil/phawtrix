package ch.phildev.springphawtrix.domain;

import java.util.HashMap;
import java.util.Map;

public enum PhawtrixCommand {
    DRAW_TEXT((byte) 0x0),
    DRAW_BMP((byte) 0x1),
    DRAW_CIRCLE((byte) 0x2),
    FILL_CIRCLE((byte) 0x3),
    DRAW_PIXEL((byte) 0x4),
    DRAW_RECT((byte) 0x5),
    DRAW_LINE((byte) 0x6),
    FILL_MATRIX((byte) 0x7),
    SHOW((byte) 0x8),
    CLEAR((byte) 0x9),
    PLAY_MP3((byte) 0xA),
    RESET((byte) 0xB),
    GET_MATRIX_INFO((byte) 0xC),
    SET_BRIGHTNESS((byte) 0xD),
    SAVE_CONFIG((byte) 0xE),
    RESET_WIFI((byte) 0xF),
    PING((byte) 0x10);

    // Create reverse-lookup Map
    static final Map<Byte, PhawtrixCommand> byteCommands = new HashMap<>();
    static {
        for (PhawtrixCommand cmd : PhawtrixCommand.values()) {
            byteCommands.put(cmd.byteValue, cmd);
        }
    }

    private final byte byteValue;

    PhawtrixCommand(byte byteValue) {
        this.byteValue = byteValue;
    }

    public int getByteValue() {
        return byteValue;
    }

    public String getAsString() {
        return Byte.toString(byteValue);
    }

    public static PhawtrixCommand of(String text) {
        byte textAsByte = Byte.parseByte(text);

        if (textAsByte > 16) {
            throw new IllegalArgumentException("Only commands up to 16 are defined");
        }

        return byteCommands.get(textAsByte);
    }
}
