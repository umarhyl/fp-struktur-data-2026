package model;

public class RouteEdge {
    private String sourceId;
    private String destinationId;
    private double distance; // weight in km
    private boolean active; // true if road is accessible, false if broken/blocked

    public RouteEdge(String sourceId, String destinationId, double distance) {
        this.sourceId = sourceId;
        this.destinationId = destinationId;
        this.distance = distance;
        this.active = true;
    }

    public String getSourceId() { return sourceId; }
    public String getDestinationId() { return destinationId; }
    public double getDistance() { return distance; }
    public boolean isActive() { return active; }

    public void setActive(boolean active) {
        this.active = active;
    }

    @Override
    public String toString() {
        return sourceId + " -> " + destinationId + " : " + distance + " km (" + (active ? "Active" : "Blocked") + ")";
    }
}
