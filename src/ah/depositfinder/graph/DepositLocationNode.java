package ah.depositfinder.graph;

public class DepositLocationNode extends LocationNode {
    public DepositLocationNode(LocationNode node) {
        super(node.getName());
        travelTimesToNeighbors.putAll(node.getTravelTimesToNeighbors());
    }

    public DepositLocationNode(String name) {
        super(name);
    }
}
