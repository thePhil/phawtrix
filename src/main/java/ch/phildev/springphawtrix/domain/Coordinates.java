package ch.phildev.springphawtrix.domain;

//@Data
//@AllArgsConstructor(staticName = "of")
public class Coordinates {

    final int x;
    final int y;

    private Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * Expect coordinates in the string "x,y" as numbers
     *
     * @param coordinates x,y coordinates
     * @return parsed as object
     */
    static Coordinates of(String coordinates) {

        String[] cords = coordinates.split(",");
        if (cords.length != 2) {
            throw new IllegalArgumentException("A coordinate must be expressed as String separated by a ','");
        }

        return Coordinates.of(Integer.parseInt(cords[0]), Integer.parseInt(cords[1]));
    }

    private static Coordinates of(int x, int y) {
        return new Coordinates(x, y);
    }


    public byte getByteX() {

        if (x > 255) {
            throw new IllegalArgumentException("only coordinates up to 255 are supported");
        }
        return (byte) x;
    }

    public byte getByteY() {
        if (y > 255) {
            throw new IllegalArgumentException("only coordinates up to 255 are supported");
        }
        return (byte) y;
    }
}
