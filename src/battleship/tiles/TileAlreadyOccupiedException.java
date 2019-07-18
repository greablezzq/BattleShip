package battleship.tiles;

public class TileAlreadyOccupiedException extends Exception {
    // Exceptions must implement the Serializable interface, which 
    // requires the addition of a unique ID (serialVersionUID)
    // Eclipse can generate that for us
    private static final long serialVersionUID = 1624174337931065145L;

    public TileAlreadyOccupiedException(String string) {
        super(string);
    }
}
