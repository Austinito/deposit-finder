package DepositFinder.graph;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

/**
 * The <code>DepositLocationGraph</code> class represents a set of <code>LocationNode</code>s.
 * Class was created with intention to hold more data, but after creating adjacency lists within
 * the <code>LocationNode</code> class itself, most data was no longer needed. <code>DepositLocationGraph</code>
 * class was kept to maintain readability and enforce desired behavior.
 *
 * @see LocationNode
 * @see GraphController
 */
public class DepositLocationGraph {
    private final Set<LocationNode> locations;

    public DepositLocationGraph() {
        locations = new HashSet<>();
    }

    public Optional<LocationNode> getNode(String name) {
        return locations.stream().filter(e -> e.getName().equals(name)).findFirst();
    }

    public boolean addNode(LocationNode node) {
        return locations.add(node);
    }


    public Set<LocationNode> getLocations() {
        return Collections.unmodifiableSet(locations);
    }
}
