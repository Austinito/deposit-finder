package DepositFinder.graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class LocationNode {
    private final String name;
    private final Map<LocationNode, Integer> travelTimesToNeighbors;

    public LocationNode(String name) {
        this.name = name;
        travelTimesToNeighbors = new HashMap<>();
    }

    /**
     * @param node          the new adjacent node
     * @param travelTime    the travel time to adjacent node
     * @return              <code>true</code> if added;
     *                      <code>false</code> if node already exists.
     */
    public boolean addAdjacentLocation(LocationNode node, int travelTime) {
        if (travelTimesToNeighbors.containsKey(node)) {
            return false;
        }
        travelTimesToNeighbors.put(node, travelTime);
        return true;
    }

    public String getName() {
        return name;
    }

    public Map<LocationNode, Integer> getTravelTimesToNeighbors() {
        return Collections.unmodifiableMap(travelTimesToNeighbors);
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
}
