package ah.depositfinder.graph;

public class Edge {
    private final LocationNode location1, location2;
    private final int travelTime;

    public Edge(LocationNode location1, LocationNode location2, int travelTime) {
        this.location1 = location1;
        this.location2 = location2;
        this.travelTime = travelTime;
    }

    public LocationNode getLocation1() {
        return location1;
    }

    public LocationNode getLocation2() {
        return location2;
    }

    public int getTravelTime() {
        return travelTime;
    }
}
