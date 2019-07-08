package battleship.utilities;

import battleship.handling.Settings;

/**
 * This class is purely a data structure; i.e. it contains information 
 * but has no behaviour of its own. Other programming languages refer to 
 * such objects as "Struct". In accordance with the Java Code 
 * Conventions from 1999, we will leave this class' fields public.
 */
public class Coordinates {

    public final int	x;
    public final int	y;

    public Coordinates(int x, int y) {
        this.x = x;
        this.y = y;
    }

    /**
     * @param input a string comprising only one letter and one digit, 
     * representing the horizontal and vertical coordinates of a tile.
     */
    public static Coordinates fromString(String input) throws Exception {
        int x, y;

        // This line uses a so-called "regular expression" to remove 
        // every part of the string that consists of anything other 
        // multiple subsequent alphanumeric characters or digits. 
        String trimmedInput = input.trim().replaceAll("[^a-zA-Z0-9]*", "");
        int len = trimmedInput.length();
        String first = trimmedInput.substring(0, 1);
        String last = trimmedInput.substring(len-1, len);
        if (first.matches("[a-zA-Z]")) {
            // first part is the (single) character
            x = StringManipulation.stringToIndex(first);
            trimmedInput = trimmedInput.substring(1);
        } else if (last.matches("[a-zA-Z]")) {
            // last part is the (single) character
            x = StringManipulation.stringToIndex(last);
            trimmedInput = trimmedInput.substring(0,len-1);
        } else 
            throw new IncomprehensibleCoordinatesException("Cannot make sense of " + input + "! Use a character combined with a number. ");

        // parse the (remaining) number part (possibly several digits)
        if (trimmedInput.matches("[0-9]+"))
            y = Integer.parseInt(trimmedInput);
        else 
            throw new IncomprehensibleCoordinatesException(
                    "Cannot make sense of " + input + "! Use a character combined with a number. ");

        if (!coordinateIsWithinPlayingField(x,y))
            throw new IncomprehensibleCoordinatesException(
                    "Coordinates " + input + " (" + x + "-" + y + ") are too far away!");

        return new Coordinates(x, y);

    }

    public static boolean coordinateIsWithinPlayingField (int x, int y) {
        return (x >= 0 && 
                y >= 0 && 
                x < Settings.PLAYING_FIELD_HORIZONTAL_SIZE && 
                y < Settings.PLAYING_FIELD_VERTICAL_SIZE);
    }

    @Override
    public String toString() {
        return StringManipulation.indexToLetter(x) + "" + y;
    }
}
