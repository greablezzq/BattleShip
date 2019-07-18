package battleship.tiles;

import battleship.players.Player;
import battleship.ships.Ship;
import battleship.utilities.Coordinates;
import battleship.utilities.StringManipulation;

/**
 * Tiles are simply data storage objects that provide information on 
 * whether a given location has been fired at and whether it contains a 
 * ship.
 */
public class Tile {

    private Coordinates	coordinates;
    protected Fleet		fleet;

    protected boolean	hasBeenFiredUpon	= false;

    public Tile(int horizontalIndex, int verticalIndex, Fleet fleet) {
        this(new Coordinates (horizontalIndex, verticalIndex), fleet);
    }

    public Tile(Coordinates coordinates, Fleet fleet) {
        this.coordinates = coordinates;
        this.fleet = fleet;
    }

    public void fireAtThisTile(Player observer) {
        if (hasBeenFiredUpon) {
            observer.youKeepFiringAtNothing(coordinates());
            player().theEnemyKeepsFiringAtNothing(this.coordinates());
        } else {
            observer.youHaveMissed(coordinates());
            player().youHaveBeenMissed(this.coordinates());
            markAsHavingBeenFiredUpon();
        }
    }

    protected void markAsHavingBeenFiredUpon() {
        this.hasBeenFiredUpon = true;
    }

    @Override
    public String toString() {
        return StringManipulation.indexToLetter(coordinates.x) + "" + coordinates.y;
    }

    public boolean hasBeenFiredUpon() {
        return this.hasBeenFiredUpon;
    }

    /**
     * @param showShips this parameter is necessary for the override found in the ShipTile subclass, but not actually used here.
     */
    String getVisualRepresentation(boolean showShips) {
        if (hasBeenFiredUpon())
            return ".";
        else
            return " ";
    }

    public int horizontalIndex() {
        return coordinates.x;
    }

    public int verticalIndex() {
        return coordinates.y;
    }

    public Coordinates coordinates() {
        return this.coordinates;
    }

    protected Player player() {
        return fleet.getPlayer();
    }

    public Ship getShip() {
        return null;
    }
}
