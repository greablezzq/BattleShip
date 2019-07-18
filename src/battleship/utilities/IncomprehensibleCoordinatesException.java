package battleship.utilities;

public class IncomprehensibleCoordinatesException extends Exception {
    // Exceptions must implement the Serializable interface, which 
    // requires the addition of a unique ID (serialVersionUID)
    // Eclipse can generate that for us
    private static final long serialVersionUID = 6274277394003551241L;

    public IncomprehensibleCoordinatesException(String string) {
        super(string);
    }
}
