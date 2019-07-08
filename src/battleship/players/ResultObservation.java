package battleship.players;

public enum ResultObservation {
    HIT("X", true),
    MISS(".", false);

    private final String marker;
    private final boolean isHit;
    
    private ResultObservation(String marker, boolean isHit) {
        this.marker = marker;
        this.isHit = isHit;
    }

    public String toString () {
        return marker;
    }

    public boolean isAHit() {
        return isHit;
    }

    public boolean isAMiss() {
        return !isHit;
    }
}
