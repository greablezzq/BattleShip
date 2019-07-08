package battleship.players;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import battleship.handling.Settings;
import battleship.ships.Ship;
import battleship.tiles.Fleet;
import battleship.tiles.Fleet.Orientation;
import battleship.tiles.InvalidFleetSetupException;
import battleship.utilities.Coordinates;
import battleship.utilities.StringManipulation;

/**
 * A player can be either human, i.e. someone giving manual input to 
 * make his moves, or a computer opponent, whose moves are made 
 * automatically. Which one it is depends on the subclass.
 */
public abstract class Player {

    protected String	name;
    protected Fleet fleet;

    /**
     * This two-dimensional array is used as the player's memory.
     * The array indices represent coordinates in the enemy's fleet,
     * and the markers are used to remember whether the tile at those coordinates contained a ship when it was fired at.
     * When an element of this array is null, that means it has not yet been fired at.
     */
    protected ResultObservation[][] resultObservations;
    protected int enemyStrenghPresumedToRemain;

    public Player(String name) {
        this.name = name;
        this.fleet = new Fleet(this);
        this.resultObservations = new ResultObservation[Settings.PLAYING_FIELD_HORIZONTAL_SIZE][Settings.PLAYING_FIELD_VERTICAL_SIZE];
        this.enemyStrenghPresumedToRemain = Settings.defaultStartingFleetStrength();
    }

    @Override
    public String toString() {
        return name;
    }

    public String getAccuracyInformation() {
        // TODO implement me
        return "";
    }

    public boolean isDefeated() {
        return fleet.isDefeated();
    }

    public abstract boolean isAnAIPlayer();

    public final void placeShips() throws Exception {
        runPlayerTypeSpecificShipPlacement();
        for (Ship ship : fleet.getShips())
            if (ship.hasBeenPlaced() == false)
                throw new InvalidFleetSetupException(fleet + " is not set up correctly; ship " + ship + " has not been placed.");
        fleet.fillAllUnoccupiedTiles();
    }

    protected abstract void runPlayerTypeSpecificShipPlacement() throws Exception;

    protected void placeShipsRandomly() throws Exception {
        for (Ship ship : fleet.getShips())
            placeShipRandomly(ship);
    }

    protected void placeShipRandomly(Ship ship) throws Exception {
        int shipLength = ship.length();
        int rightBoundsIfHorizontal = Settings.PLAYING_FIELD_HORIZONTAL_SIZE - shipLength;
        int lowerBoundsIfVertical = Settings.PLAYING_FIELD_VERTICAL_SIZE - shipLength;
        Random random = new Random();

        for (int attempts = 100; attempts > 0; attempts--) {
            Orientation orientation = random.nextBoolean() ? Orientation.HORIZONTAL : Orientation.VERTICAL;
            int x = random.nextInt(orientation == Orientation.HORIZONTAL ? rightBoundsIfHorizontal
                    : Settings.PLAYING_FIELD_HORIZONTAL_SIZE);
            int y = random.nextInt(
                    orientation == Orientation.VERTICAL ? lowerBoundsIfVertical : Settings.PLAYING_FIELD_VERTICAL_SIZE);

            Coordinates shipPlacementCoordinates = new Coordinates(x,y);
            if (fleet.potentialShipLocationIsFree(shipPlacementCoordinates, shipLength, orientation)) {
                fleet.placeShip(ship, shipPlacementCoordinates, shipLength, orientation);
                return;
            }
        }
        throw new Exception("Failed to place ship.");
    }

    public String generateOneLineOfTheVisualFleetRepresentation(int line, boolean showShips) {
        return fleet.generateOneLineOfTheVisualRepresentation(line, showShips);
    }

    public int currentFleetStrength() {
        return fleet.currentFleetStrength();
    }

    public int originalFleetStrength() {
        return fleet.originalFleetStrength();
    }

    public Fleet getFleet() {
        return this.fleet;
    }

    public abstract Coordinates promptToFireShot() throws Exception;

    protected Coordinates fireAtRandomTarget() throws Exception {
        Random random = new Random();
        Coordinates coordinates;

        List<Coordinates> rejectedCoordinates = new ArrayList<Coordinates>();

        for (int attempts = Settings.PLAYING_FIELD_HORIZONTAL_SIZE * Settings.PLAYING_FIELD_VERTICAL_SIZE * 10; attempts > 0; attempts--) {
            coordinates = new Coordinates(
                    random.nextInt(Settings.PLAYING_FIELD_HORIZONTAL_SIZE),
                    random.nextInt(Settings.PLAYING_FIELD_VERTICAL_SIZE));

            boolean coordinatesHaveNotYetBeenFiredUpon = resultObservations[coordinates.x][coordinates.y] == null;
            boolean fireAtTheseCoordinates = coordinatesHaveNotYetBeenFiredUpon;
            if (coordinatesHaveNotYetBeenFiredUpon == false) {
                rejectedCoordinates.add(coordinates);
                attempts--;
            }
            if (fireAtTheseCoordinates)
                return coordinates;
        }
        throw new Exception("Attempts depleted, no firing solution found.");
    }

    public void receiveFireAtCoordinates (Player attacker, Coordinates coordinates){
        fleet.receiveFireAtCoordinates(attacker, coordinates);
    }

    public String createOneLineOfTheVisualRepresentationOfObservedResults (int lineNumber) {
        // Start each line with its number
        String line = lineNumber + "" + StringManipulation.TAB;
        for (int x = 0; x < Settings.PLAYING_FIELD_HORIZONTAL_SIZE; x++) 
            line += resultObservations[x][lineNumber] + StringManipulation.TAB;
        return line;
    }

    // --------------------------------------------------
    // Events
    // --------------------------------------------------
    public abstract void yourTurnHasBegun();

    public abstract void youHaveBeenMissed(Coordinates coordinates);

    public abstract void youHaveBeenHit(Coordinates coordinates, Ship ship);

    public abstract void yourShipHasBeenSunk(Coordinates lastHit, Ship ship);


    public void youHaveMissed(Coordinates coordinates) {
        this.resultObservations[coordinates.x][coordinates.y] = ResultObservation.MISS;
    }

    public void youHaveHitYourTarget(Coordinates coordinates) {
        this.enemyStrenghPresumedToRemain -= 1;
        this.resultObservations[coordinates.x][coordinates.y] = ResultObservation.HIT;
    }

    public void youHaveSunkAnEnemyShip(Coordinates lastHit, Ship ship) {
        this.enemyStrenghPresumedToRemain -= 1;
        this.resultObservations[lastHit.x][lastHit.y] = ResultObservation.HIT;
    }

    public abstract void youKeepFiringAtASunkShip(Coordinates lastHit, Ship ship);

    public abstract void youKeepFiringAtTheSameHole(Coordinates lastHit);

    public abstract void youKeepFiringAtNothing(Coordinates lastHit);

    public abstract void theEnemyKeepsFiringAtASunkShip(Coordinates lastHit, Ship ship);

    public abstract void theEnemyKeepsFiringAtTheSameHole(Coordinates lastHit, Ship ship);

    public abstract void theEnemyKeepsFiringAtNothing(Coordinates lastHit);
}
