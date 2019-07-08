package battleship.utilities;

import battleship.handling.Settings;

public class StringManipulation {

    public final static String	TAB						= "\t";
    private static final int	INT_TO_LETTER_OFFSET	        = 65;
    private static String fleetRepresentationHeadingLine = null;

    public static char indexToLetter(int index) {
        return (char) (index + INT_TO_LETTER_OFFSET);
    }

    public static int charToIndex(char character) {
        character = ("" + character).toUpperCase().charAt(0); // Make sure it's a capital letter.
        return (character) - INT_TO_LETTER_OFFSET;
    }

    public static int stringToIndex(String string) throws IllegalArgumentException {
        if (string.length() > 1)
            throw new IllegalArgumentException("Cannot parse coordinates with more than one letter.");
        return charToIndex(string.charAt(0));
    }

    public static String generateFleetRepresentationHeadingLine() {
        // This function uses a technique called Lazy Instantiation.
        // We only initialize our field when we first need it.
        // If we never need it, we never instantiate it.
        // If we need it repeatedly, it's instantiated the first time, 
        // and all future calls will simply return the saved value.
        if (fleetRepresentationHeadingLine == null) {
            fleetRepresentationHeadingLine = "";
            // Leave one extra free spot for the line numbers
            fleetRepresentationHeadingLine += StringManipulation.TAB;
            // Generate Headings
            for (int horizontal = 0; horizontal < Settings.PLAYING_FIELD_HORIZONTAL_SIZE; horizontal++)
                fleetRepresentationHeadingLine += StringManipulation.indexToLetter(horizontal) + StringManipulation.TAB;
        }
        return fleetRepresentationHeadingLine;
    }
}
