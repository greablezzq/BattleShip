package battleship.tiles;

public class InvalidFleetSetupException extends Exception {
    // Exceptions must implement the Serializable interface, which 
    // requires the addition of a unique ID (serialVersionUID)
    // Eclipse can generate that for us
    private static final long serialVersionUID = -7060034877708165091L;

    public InvalidFleetSetupException(String string) {
        super(string);
    }
}
