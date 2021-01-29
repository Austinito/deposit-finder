package DepositFinder.graph;

/**
 *  The <code>DepositLocationNode</code> class is a <code>LocationNode</code> that has a known
 *  location name defined by <code>DepositLocation</code> enum.
 *
 * @see LocationNode
 * @see DepositLocation
 */
public class DepositLocationNode extends LocationNode {
    public DepositLocationNode(String name) {
        super(name);
    }
}
