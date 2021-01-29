package DepositFinder.graph;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 *  The <code>LocationNode</code> class represents a location with a name. A <code>LocationNode</code> is
 *  able to keep a of map its own neighbors with its associated travel time.
 *
 * @see TravelTime
 */
public class LocationNode {
    private final String name;
    private final Map<LocationNode, Integer> travelTimesToNeighbors;

    public LocationNode(String name) {
        this.name = name;
        travelTimesToNeighbors = new HashMap<>();
    }

    /**
     * Returns true if node was mapped as a new neighbor with the specified travel time.
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

    /**
     * Compares this Location node to the specified object. The <code>LocationNodes</code> are considered
     * equal if they have the same name.
     * @param o The object to compare this <code>LocationNode</code> against
     * @return  <code>true</code> if the given object represents a <code>LocationNode</code> with the same
     *          name. <code>false</code> otherwise.
     */
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
