package battleship.handling;

public class Settings {

    /**
     * Horizontal coordinates are read as letters; so don't make this bigger than
     * the alphabet. But please also don't make it smaller than 2.
     */
    public static final int		PLAYING_FIELD_HORIZONTAL_SIZE	= 10;

    /**
     * Please don't make this smaller than 2.
     */
    public static final int		PLAYING_FIELD_VERTICAL_SIZE	= 10;

    public static final int		MAXIMUM_PLAYABLE_TURNS		= 10 * Settings.PLAYING_FIELD_HORIZONTAL_SIZE
            * Settings.PLAYING_FIELD_VERTICAL_SIZE;

    public static final boolean	ALLOW_ADJACENT_SHIP_PLACEMENT = false;

    public static final int		ONE_TILE_SHIPS				= 0;									// Default: 0
    public static final int		TWO_TILE_SHIPS				= 4;									// Default: 4
    public static final int		THREE_TILE_SHIPS			= 3;									// Default: 3
    public static final int		FOUR_TILE_SHIPS				= 2;									// Default: 2
    public static final int		FIVE_TILE_SHIPS				= 1;									// Default: 1

    public static final String	ONE_TILE_SHIP_NAME			= "One";//"Submarine";
    public static final String	TWO_TILE_SHIP_NAME			= "Destroyer";
    public static final String	THREE_TILE_SHIP_NAME		    = "Cruiser";
    public static final String	FOUR_TILE_SHIP_NAME			= "Battleship";
    public static final String	FIVE_TILE_SHIP_NAME			= "Carrier";
    public static final String   OTHER_TILE_SHIP_NAME        = "Man-of-war";

    public static int defaultStartingFleetStrength () {
        return 
                (ONE_TILE_SHIPS * 1) + 
                (TWO_TILE_SHIPS * 2) + 
                (THREE_TILE_SHIPS * 3) + 
                (FOUR_TILE_SHIPS * 4) + 
                (FIVE_TILE_SHIPS * 5);
    }
}
