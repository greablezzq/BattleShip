package battleship.ships;

import java.util.HashSet;
import java.util.Set;

import battleship.handling.Settings;
import battleship.tiles.ShipTile;

public class Ship {

    private boolean hasBeenPlaced = false;
    private Set<ShipTile> tiles;
    private int length;

    public Ship(int length) {
        this.length = length;
        this.tiles = new HashSet<ShipTile>();
    }

    public Set<ShipTile> getTiles() {
        return tiles;
    }

    public boolean isSunk() {
        return strength() <= 0;
    }

    public int strength() {
        int sum = 0;
        for (ShipTile tile : tiles)
            if (!tile.hasBeenFiredUpon())
                sum++;
        return sum;
    }

    public String getVisualRepresentation() {
        return tiles.size() + "";
    }

    public void markShipAsPlaced() {
        this.hasBeenPlaced = true;
    }

    public boolean hasBeenPlaced() {
        return this.hasBeenPlaced;
    }

    public int length() {
        return length;
    }

    public void associateWithTile(ShipTile tile) {
        this.tiles.add(tile);
    }

    @Override
    public String toString() {
        switch (length) {
        case 1:
            return Settings.ONE_TILE_SHIP_NAME;
        case 2:
            return Settings.TWO_TILE_SHIP_NAME;
        case 3:
            return Settings.THREE_TILE_SHIP_NAME;
        case 4:
            return Settings.FOUR_TILE_SHIP_NAME;
        case 5:
            return Settings.FIVE_TILE_SHIP_NAME;
        default:
            return Settings.OTHER_TILE_SHIP_NAME;
        }

    }

}
