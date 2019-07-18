package battleship.tiles;

import java.util.HashSet;
import java.util.Set;

import battleship.handling.Settings;
import battleship.players.Player;
import battleship.ships.Ship;
import battleship.ships.ShipAlreadyPlacedException;
import battleship.utilities.Coordinates;
import battleship.utilities.StringManipulation;

/**
 * A fleet represents all a player's ships and their locations on a two-dimensional grid of tiles.
 */
public class Fleet {

    /**
     * This list contains all the player's ships, regardless of their state.
     */
    private Set<Ship>	ships;

    /**
     * This is simply the player to whom this fleet belongs.
     */
    private Player		player;

    /**
     * How to assemble your fleet: 1. Construct a player. This automatically
     * constructs an empty fleet belonging to that player. 2. Place ships in the
     * fleet, using the OccupyTile function. 3. Fill up all empty tiles with the
     * FillAllUnoccupiedTiles function.
     */

    /**
     * This two-dimensional array of tiles is where the locations of the player's ships are stored.
     */
    private Tile[][] tiles;

    public enum Orientation {
        VERTICAL, HORIZONTAL
    }

    public Fleet(Player player) {
        this.player = player;
        this.tiles = new Tile[Settings.PLAYING_FIELD_HORIZONTAL_SIZE][Settings.PLAYING_FIELD_VERTICAL_SIZE];
        this.ships = new HashSet<Ship>();
        for (int i = 0; i < Settings.FIVE_TILE_SHIPS; i++)
            ships.add(new Ship(5));
        for (int i = 0; i < Settings.FOUR_TILE_SHIPS; i++)
            ships.add(new Ship(4));
        for (int i = 0; i < Settings.THREE_TILE_SHIPS; i++)
            ships.add(new Ship(3));
        for (int i = 0; i < Settings.TWO_TILE_SHIPS; i++)
            ships.add(new Ship(2));
        for (int i = 0; i < Settings.ONE_TILE_SHIPS; i++)
            ships.add(new Ship(1));
    }

    /**
     * @return returns the actual list, not a copy.
     */
    public Set<Ship> getShips() {
        return this.ships;
    }

    public boolean allShipsHaveBeenPlaced() {
        for (Ship ship : getShips())
            if (!ship.hasBeenPlaced())
                return false;
        return true;
    }

    public Ship getShipAtCoordinates(Coordinates coordinates) {
        return getTile(coordinates).getShip();
    }

    public int originalFleetStrength() {
    	int original=0;
    	for (Ship ship:ships) {
    		original += ship.length();
    	}
        return original;
    }

    public int currentFleetStrength() {
    	int current=0;
    	for (Ship ship:ships) {
    		current += ship.strength();
    	}
    	return current;
    }

    /**
     * Ships are placed by specifying coordinates; these
     * will be the ship's upper and left end. Orientation and length specify in which
     * direction the rest of the ship goes.
     */
    public boolean potentialShipLocationIsFree(Coordinates coordinates, 
            int length, Orientation orientation) throws Exception {

        int x = coordinates.x;
        int y = coordinates.y;
        for (int i = 0; i < length; i++) {
            if (!tileIsFree(x,y)) 
                return false;
            // If applicable, check for ships being too close together.
            if (!Settings.ALLOW_ADJACENT_SHIP_PLACEMENT && 
                    ((Coordinates.coordinateIsWithinPlayingField(x-1, y) && !tileIsFree(x-1,y)) ||
                            (Coordinates.coordinateIsWithinPlayingField(x+1, y) && !tileIsFree(x+1,y)) ||
                            (Coordinates.coordinateIsWithinPlayingField(x, y-1) && !tileIsFree(x,y-1)) ||
                            (Coordinates.coordinateIsWithinPlayingField(x, y+1) && !tileIsFree(x,y+1))))
                return false;

            if (orientation == Orientation.HORIZONTAL)
                x++;
            else 
                y++;
        }
        return true;
    }

    private boolean tileIsFree (int x, int y) {
        return getTile(x, y) == null;
    }

    public boolean coordinatesHaveBeenShotAt(Coordinates coordinates) {
        return getTile(coordinates).hasBeenFiredUpon();
    }

    public void placeShip(Ship ship, Coordinates coordinates, 
            int length, Orientation orientation) 
            throws TileAlreadyOccupiedException, ShipAlreadyPlacedException {

        if (ship.hasBeenPlaced())
            throw new ShipAlreadyPlacedException("Ship " + ship + " has already been placed!");

        int x = coordinates.x;
        int y = coordinates.y;
        for (int i = 0; i < length; i++) {
            if (getTile(x, y) != null)
                throw new TileAlreadyOccupiedException("(" + x + "/" + y + ")");
            occupyTile(new Coordinates(x,y), ship);
            if (orientation == Orientation.HORIZONTAL)
                x++;
            else
                y++;
        }
        ship.markShipAsPlaced();
    }

    private void occupyTile(Coordinates coordinates, Ship ship) 
            throws TileAlreadyOccupiedException, ShipAlreadyPlacedException {
        
        if (getTile(coordinates) != null)
            throw new TileAlreadyOccupiedException(
                    "Tile " + getTile(coordinates) + " is already occupied!");

        ShipTile shipTile = new ShipTile(coordinates, ship, this);
        setTile(coordinates, shipTile);
    }

    private Tile getTile(Coordinates coordinates) {
        return getTile(coordinates.x, coordinates.y);
    }

    private Tile getTile(int horizontalIndex, int verticalIndex) throws ArrayIndexOutOfBoundsException {
        try {
            return tiles[horizontalIndex][verticalIndex];
        } catch (ArrayIndexOutOfBoundsException aioobe) {
            throw new ArrayIndexOutOfBoundsException("(" + horizontalIndex + "/" + verticalIndex + ")");
        }
    }

    private void setTile (Coordinates coordinates, Tile value) {
        this.setTile(coordinates.x, coordinates.y, value);
    }

    private void setTile(int horizontalIndex, int verticalIndex, Tile value) {
        tiles[horizontalIndex][verticalIndex] = value;
    }

    public void fillAllUnoccupiedTiles() {
        for (int horizontal = 0; horizontal < Settings.PLAYING_FIELD_HORIZONTAL_SIZE; horizontal++)
            for (int vertical = 0; vertical < Settings.PLAYING_FIELD_VERTICAL_SIZE; vertical++)
                if (getTile(horizontal, vertical) == null)
                    setTile(horizontal, vertical, new Tile(horizontal, vertical, this));
    }

    public String generateOneLineOfTheVisualRepresentation(int lineNumber, boolean showShips) {
        String line = "";
        // Start each line with its number
        line += lineNumber + "" + StringManipulation.TAB;
        // Then add each horizontal step's tile
        for (int horizontal = 0; horizontal < Settings.PLAYING_FIELD_HORIZONTAL_SIZE; horizontal++) {
            Tile tile = getTile(horizontal, lineNumber);
            line += tile.getVisualRepresentation(showShips) + StringManipulation.TAB;
        }
        return line;
    }

    public void receiveFireAtCoordinates(Player observer, Coordinates coordinates) {
        getTile(coordinates.x, coordinates.y).fireAtThisTile(observer);
    }

    public boolean isDefeated() {
        for (Ship ship : ships)
            if (!ship.isSunk())
                return false;
        return true;
    }

    @Override
    public String toString() {
        return "the " + this.player + " Fleet";
    }

    public Player getPlayer() {
        return this.player;
    }
}
