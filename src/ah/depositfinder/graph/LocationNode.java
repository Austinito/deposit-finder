package ah.depositfinder.graph;

import java.util.HashMap;
import java.util.Map;

public class LocationNode {
    private String name;
    protected Map<LocationNode, Integer> travelTimesToNeighbors;

    public LocationNode(String name) {
        this.name = name;
        travelTimesToNeighbors = new HashMap<>();
    }

    public void addAdjacentLocation(LocationNode node, int travelTime) {
        if (travelTimesToNeighbors.containsKey(node)) {
            throw new IllegalArgumentException("Error: Cannot add another path where one already exists.");
        }
        travelTimesToNeighbors.put(node, travelTime);
    }

    public String getName() {
        return name;
    }

    // If Location Nodes have the same name, they are treated as if they are equal.
    @Override
    public boolean equals(Object o) {
        if (o == this) {
            return true;
        }

        if (!(o instanceof LocationNode)) {
            return false;
        }

        LocationNode otherNode = (LocationNode) o;
        return name.equals(otherNode.getName());
    }

    @Override
    public int hashCode() {
        return name.hashCode();
    }


    public Map<LocationNode, Integer> getTravelTimesToNeighbors() {
        return travelTimesToNeighbors;
    }
}
