package ah.depositfinder.graph;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class DepositLocationGraph {
    private final List<LocationNode> locations;

    public DepositLocationGraph() {
        locations = new ArrayList<>();
    }

    public Optional<LocationNode> getNode(String name) {
        for (LocationNode location : locations) {
            if (name.equals(location.getName())) {
                return Optional.of(location);
            }
        }
        return Optional.empty();
    }

    public void addNode(LocationNode node) throws IllegalArgumentException {
        if (!locations.contains(node)) {
            locations.add(node);
        } else {
            throw new IllegalArgumentException("Asked to add a node that already exists: " + node);
        }
    }

    public List<LocationNode> getLocations() {
        return locations;
    }
}
