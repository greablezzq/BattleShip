package battleship.ships;

public class ShipAlreadyPlacedException extends Exception {
    // Exceptions must implement the Serializable interface, which 
    // requires the addition of a unique ID (serialVersionUID)
    // Eclipse can generate that for us
    private static final long serialVersionUID = -1567857687791646227L;

    public ShipAlreadyPlacedException(String string) {
		super(string);
	}
}
