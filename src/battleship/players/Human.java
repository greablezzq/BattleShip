package battleship.players;

import java.util.ArrayList;
import java.util.List;

import battleship.handling.GameHandler;
import battleship.handling.Settings;
import battleship.ships.Ship;
import battleship.utilities.Coordinates;
import battleship.utilities.IncomprehensibleCoordinatesException;
import battleship.utilities.Input;
import battleship.utilities.StringManipulation;

/**
 * This class represents an actual human player. When a human player's 
 * turn comes, there are no automatic decisions by an AI. Instead, the 
 * game requests manual input from the user.
 */
public class Human extends Player {

    private List<String> messages;

    public Human(String name) {
        super(name);
        messages = new ArrayList<String>();
    }

    @Override
    public String toString() {
        return super.toString() + " (human)";
    }

    @Override
    public void runPlayerTypeSpecificShipPlacement() throws Exception {
        super.placeShipsRandomly();
    }

    @Override
    public boolean isAnAIPlayer() {
        return false;
    }


    @Override
    public void yourTurnHasBegun() {
        printMessages();
    }

    // ----------------------------------------------------------------------------------------------------
    // Choosing targets
    // ----------------------------------------------------------------------------------------------------

    @Override
    public Coordinates promptToFireShot() throws Exception {
        return promptToFireShot(3);
    }

    public Coordinates promptToFireShot(int attemptsRemaining) throws Exception {
        System.out.println("Choose your target. (" 
                + StringManipulation.indexToLetter(0) + "0" + " to "
                + StringManipulation.indexToLetter(Settings.PLAYING_FIELD_HORIZONTAL_SIZE - 1)
                + (Settings.PLAYING_FIELD_VERTICAL_SIZE - 1) + ")");
        try {
            Coordinates coordinates = Input.nextCoordinates();
            if (resultObservations[coordinates.x][coordinates.y] != null) {
                // You are now firing at a target that has already been shot.
            }
            System.out.println("Target: " + coordinates + ". FIRE!");
            return coordinates;
        } catch (IncomprehensibleCoordinatesException ice) {
            return firingOrderMisunderstood(ice.getMessage(), attemptsRemaining);
        }
    }

    private Coordinates firingOrderMisunderstood(String reasonForMisunderstanding, int attemptsRemaining)
            throws Exception {

        System.out.print("Admiral, we cannot do that! " + reasonForMisunderstanding + " ");
        if (attemptsRemaining > 1) {
            System.out.println("Please reaffirm your order!");
            return promptToFireShot(attemptsRemaining - 1);
        } else if (attemptsRemaining == 1) {
            System.out.println("A valid order is urgently required!");
            return promptToFireShot(attemptsRemaining - 1);
        } else {
            System.out.println("Radio contact lost! Fire at will!");
            return fireAtRandomTarget();
        }
    }

    // ----------------------------------------------------------------------------------------------------
    // Receiving messages
    // ----------------------------------------------------------------------------------------------------

    private void logAMessage(String message) {
        this.messages.add(message);
    }

    private void printMessages() {
        while (messages.size() > 0) {
            System.out.println(messages.get(0));
            messages.remove(0);
            GameHandler.drawAFancyLine();
        }
    }

    @Override
    public void youHaveBeenMissed(Coordinates coordinates) {
        logAMessage("HAH! The enemy has fired at " + coordinates + ", but we dodged the shot!");
    }

    @Override
    public void youHaveBeenHit(Coordinates coordinates, Ship ship) {
        logAMessage("DAMN! The enemy got lucky; " + ship + " has been hit in " + coordinates + ".");
    }

    @Override
    public void yourShipHasBeenSunk(Coordinates lastHit, Ship ship) {
        logAMessage("DISASTER! The " + ship + " has been sunk by enemy fire in " + lastHit + "!");
    }

    @Override
    public void youHaveMissed(Coordinates coordinates) {
        super.youHaveMissed(coordinates);
        logAMessage("SPLASH! Our salvo against " + coordinates + " has produced no observable results.");
    }

    @Override
    public void youHaveHitYourTarget(Coordinates coordinates) {
        super.youHaveHitYourTarget(coordinates);
        logAMessage("SUCCESS! We have struck an enemy vessel at " + coordinates + "! Keep her firing!");
    }

    @Override
    public void youHaveSunkAnEnemyShip(Coordinates lastHit, Ship ship) {
        super.youHaveSunkAnEnemyShip(lastHit, ship);
        logAMessage("GLORIA! We have sunk the " + ship + " in " + lastHit + "! Onward to new targets!");
    }

    @Override
    public void youKeepFiringAtASunkShip(Coordinates lastHit, Ship ship) {
        logAMessage("SIR! The " + ship + " in " + lastHit + " has already been sunk. Further fire is unnecessary!");
    }

    @Override
    public void youKeepFiringAtTheSameHole(Coordinates lastHit) {
        logAMessage("SIR! We have already struck " + lastHit + "! New targets await our attention!");
    }

    @Override
    public void youKeepFiringAtNothing(Coordinates lastHit) {
        logAMessage("SIR! Observers have reported that " + lastHit + " is entirely emtpy!");
    }

    @Override
    public void theEnemyKeepsFiringAtASunkShip(Coordinates lastHit, Ship ship) {
        logAMessage("DISGRACEFUL! The enemy keeps on shelling our sunk vessel " + ship + " in " + lastHit + ".");
    }

    @Override
    public void theEnemyKeepsFiringAtTheSameHole(Coordinates lastHit, Ship ship) {
        logAMessage("QUESTIONABLE! It seems the enemy do not trust their shells. They have hit " + ship + " in "
                + lastHit + ", again.");
    }

    @Override
    public void theEnemyKeepsFiringAtNothing(Coordinates lastHit) {
        logAMessage("WHAT? The enemy has fired at " + lastHit + ", once again wasting shells and time!");
    }
}
