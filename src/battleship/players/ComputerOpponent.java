package battleship.players;

import battleship.ships.Ship;
import battleship.utilities.Coordinates;

public class ComputerOpponent extends Player {

    public ComputerOpponent(String name) {
        super(name);
    }

    @Override
    public String toString() {
        return super.toString() + " (AI)";
    }

    @Override
    public void runPlayerTypeSpecificShipPlacement() throws Exception {
        super.placeShipsRandomly();
    }

    @Override
    final public boolean isAnAIPlayer() {
        return true;
    }

    @Override
    public void yourTurnHasBegun() {
        // TODO Auto-generated method stub
    }

    @Override
    public Coordinates promptToFireShot() throws Exception {
        return fireAtRandomTarget();
    }

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
