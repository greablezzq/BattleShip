package battleship.players;

import battleship.ships.Ship;
import battleship.utilities.Coordinates;

/**
 * This class lets you make your own AI for a computer-controlled player.
 * Read the notes below to find out what you should rewrite.
 */
public class CustomComputerOpponent extends ComputerOpponent {

    /**
     * This is this class' constructor.
     * @param name The player's name.
     */
    public CustomComputerOpponent(String name) {
        super(name); // This calls the base class' constructor, which calls its base class' constructor. You should keep this line.
    }

    /**
	 toString() overrides an object's behavior when it is cast to or used as a string.
     */
    @Override
    public String toString() {
        return name + " (Custom AI)";
    }

    // ------------------------------------------------------------------------------------------------
    // In runPlayerTypeSpecificShipPlacement() you can change how the AI places its ships.
    // ------------------------------------------------------------------------------------------------

    /**
     * This method handles the placement of all ships available to this player.
     * It overrides ComputerOpponent's default behavior, which is to place all ships randomly.
     * If you want to keep that behaviour, you can call super.placeShipsRandomly().
     * 
     * If you want to write your own ship placement procedure, these are the rules:
     * 1. You must place all the ships contained in the player's fleet's ship list. You can get this list by calling fleet.getShips().
     * 2. You must place every ship exactly once. You must not place any ship twice, or leave any ships unplaced.
     * 3. Ships may not overlap.
     * 4. Ships must be entirely within the playing field. To get the playing field's extents, get Settings.PLAYING_FIELD_HORIZONTAL_SIZE and Settings.PLAYING_FIELD_VERTICAL_SIZE. 
     *
     * Breaking any of these rules will throw an exception and abort the game.
     * 
     * To actually place a ship, call placeShip(Ship ship, Coordinates coordinates, int length, Orientation orientation).
     * To check whether a spot is free, call fleet.locationIsFree(Coordinates coordinates, int length, Orientation orientation).
     */
    @Override
    public void runPlayerTypeSpecificShipPlacement() throws Exception {
        super.runPlayerTypeSpecificShipPlacement();	// This calls the base class' implementation of this method, in which all ships are placed randomly.
    }

    // ------------------------------------------------------------------------------------------------
    // In the methods below you can let your AI decide its next move.
    // ------------------------------------------------------------------------------------------------

    /**
     * This method is called whenever your turn begins, but before being prompted to make your shot.
     * You could use it to print some information about your AI's state.
     */
    @Override
    public void yourTurnHasBegun() {
        // TODO Auto-generated method stub
    }

    /**
     * 
     * @param target This is the enemy fleet, and you can use this reference to look at all its ships. But you shouldn't do that, because that would be cheating. That you can do this is actually bad programming on my part; it shouldn't be possible! So just be nice and don't cheat.
     */
    @Override
    public Coordinates promptToFireShot() throws Exception {
        return fireAtRandomTarget();
    }


    // ------------------------------------------------------------------------------------------------
    // The methods below here are called when either player has fired a shot.
    // Use these to see how your strategy is working out, and to adjust it.
    // The calls to base functions (super.something) feed information to the base player class' results observation.
    // You don't have to use it, but be aware:
    // Without calling these base functions at the appropriate times, the default results observation will not work and you have to make your own.
    // ------------------------------------------------------------------------------------------------

    @Override
    public void youHaveBeenMissed(Coordinates coordinates) {
        // TODO Auto-generated method stub
    }

    @Override
    public void youHaveBeenHit(Coordinates coordinates, Ship ship) {
        // TODO Auto-generated method stub
    }

    @Override
    public void yourShipHasBeenSunk(Coordinates lastHit, Ship ship) {
        // TODO Auto-generated method stub
    }

    @Override
    public void youHaveMissed(Coordinates coordinates) {
        super.youHaveMissed(coordinates);
        // TODO Auto-generated method stub
    }

    @Override
    public void youHaveHitYourTarget(Coordinates coordinates) {
        super.youHaveHitYourTarget(coordinates);
        // TODO Auto-generated method stub
    }

    @Override
    public void youHaveSunkAnEnemyShip(Coordinates lastHit, Ship ship) {
        super.youHaveSunkAnEnemyShip(lastHit, ship);
        // TODO Auto-generated method stub
    }

    @Override
    public void youKeepFiringAtASunkShip(Coordinates lastHit, Ship ship) {
        // TODO Auto-generated method stub
    }

    @Override
    public void youKeepFiringAtTheSameHole(Coordinates lastHit) {
        // TODO Auto-generated method stub
    }

    @Override
    public void youKeepFiringAtNothing(Coordinates lastHit) {
        // TODO Auto-generated method stub
    }

    @Override
    public void theEnemyKeepsFiringAtASunkShip(Coordinates lastHit, Ship ship) {
        // TODO Auto-generated method stub
    }

    @Override
    public void theEnemyKeepsFiringAtTheSameHole(Coordinates lastHit, Ship ship) {
        // TODO Auto-generated method stub
    }

    @Override
    public void theEnemyKeepsFiringAtNothing(Coordinates lastHit) {
        // TODO Auto-generated method stub
    }
}
