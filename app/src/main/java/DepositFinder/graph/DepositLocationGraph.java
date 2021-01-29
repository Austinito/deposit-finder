package DepositFinder.graph;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

public class DepositLocationGraph {
    private final Set<LocationNode> locations;

    public DepositLocationGraph() {
        locations = new HashSet<>();
    }

    public Optional<LocationNode> getNode(String name) {
        for (LocationNode location : locations) {
            if (name.equals(location.getName())) {
                return Optional.of(location);
            }
        }
        return Optional.empty();
    }

    public boolean addNode(LocationNode node) {
        return locations.add(node);
    }


    public Set<LocationNode> getLocations() {
        return Collections.unmodifiableSet(locations);
    }
}
