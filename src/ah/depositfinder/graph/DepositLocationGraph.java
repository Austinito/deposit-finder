package ah.depositfinder.graph;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class DepositLocationGraph {
    private List<LocationNode> locations;
    private Map<LocationNode, Deque<LocationNode>> routesToNearestDeposits;
    private Map<LocationNode, Integer> timesToNearestDeposits;

    public DepositLocationGraph() {
        locations = new ArrayList<>();
        routesToNearestDeposits = new HashMap<>();
        timesToNearestDeposits = new HashMap<>();
    }

    public void addEdge(Edge newEdge) throws IllegalArgumentException {
        if (!locations.contains(newEdge.getLocation1()) || !locations.contains(newEdge.getLocation2())) {
            throw new IllegalArgumentException("Cannot add edge to a node that does not exist within Deposit " +
                    "Location Graph.");
        }
        if (newEdge.getTravelTime() < 0) {
            throw new IllegalArgumentException("Cannot add an edge with negative travel time!");
        }
        LocationNode n1 = newEdge.getLocation1();
        LocationNode n2 = newEdge.getLocation2();
        int travelTime = newEdge.getTravelTime();
        n1.addAdjacentLocation(n2, travelTime);
        n2.addAdjacentLocation(n1, travelTime);
    }

    public Optional<LocationNode> getNode(LocationNode node) {
        for (LocationNode location : locations) {
            if (node.equals(location)) {
                return Optional.of(location);
            }
        }
        return Optional.empty();
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

    public Deque<LocationNode> getRouteToNearestDepositLocation(LocationNode origin) {
        if (!routesToNearestDeposits.containsKey(origin)) {
            findRouteToNearestDepositLocation(origin);
        }
        return routesToNearestDeposits.get(origin);
    }

    public int getTravelTimeToNearestDepositLocation(LocationNode origin) {
        if (!timesToNearestDeposits.containsKey(origin)) {
            findRouteToNearestDepositLocation(origin);
        }
        return timesToNearestDeposits.get(origin);
    }

    // finds the minimum travel time from origin node to any deposit location
    // and stores the route to take along with given travel time.
    private void findRouteToNearestDepositLocation(LocationNode origin) {
        List<LocationNode> markedNodes = new ArrayList<>();
        Map<LocationNode, LocationNode> nodeParents = new HashMap<>();
        Map<LocationNode, Integer> shortestTimesMap = new HashMap<>();
        Map<LocationNode, Integer> closestNodeNeighbors;
        Deque<LocationNode> route = new LinkedList<>();
        LocationNode closestUnmarkedNode = origin;
        LocationNode nearestDeposit;
        int pathToNode;
        int timeToClosestNode = 0;
        int timeToDepositLocation = Integer.MAX_VALUE;
        boolean depositLocationFound = false;

        for (LocationNode location : locations) {
            shortestTimesMap.put(location, Integer.MAX_VALUE);
        }

        shortestTimesMap.put(origin, 0);

        while (!depositLocationFound || timeToClosestNode < timeToDepositLocation) {
            closestNodeNeighbors = closestUnmarkedNode.getTravelTimesToNeighbors();

            for (LocationNode node : closestNodeNeighbors.keySet()) {
                pathToNode = timeToClosestNode + closestNodeNeighbors.get(node);
                if (shortestTimesMap.get(node) > pathToNode) {
                    shortestTimesMap.replace(node, pathToNode);
                    if (nodeParents.containsKey(node)) {
                        nodeParents.replace(node, closestUnmarkedNode);
                    } else {
                        nodeParents.put(node, closestUnmarkedNode);
                    }
                }
                if (node instanceof DepositLocationNode) {
                    depositLocationFound = true;
                    if (timeToDepositLocation > pathToNode) {
                        route.clear();
                        route.addFirst(node);
                        timeToDepositLocation = pathToNode;
                    }
                }
            }
            markedNodes.add(closestUnmarkedNode);

            // pick shortest path from map;
            closestUnmarkedNode = getClosestUnmarkedNode(shortestTimesMap, markedNodes);
            timeToClosestNode = shortestTimesMap.get(closestUnmarkedNode);
        }

        // build route
        LocationNode parentNode = nodeParents.get(route.getFirst());
        while (parentNode != origin) {
            route.addFirst(parentNode);
            parentNode = nodeParents.get(parentNode);
        }
        route.addFirst(origin);

        routesToNearestDeposits.put(origin, route);
        timesToNearestDeposits.put(origin, timeToDepositLocation);
    }

    private LocationNode getClosestUnmarkedNode(Map<LocationNode, Integer> map, List<LocationNode> markedNodes) {
        Map.Entry<LocationNode, Integer> min = null;
        for (Map.Entry<LocationNode, Integer> entry : map.entrySet()) {
            if ((min == null) || min.getValue() > entry.getValue()) {
                if (!markedNodes.contains(entry.getKey())) {
                    min = entry;
                }
            }
        }
        return min.getKey();
    }
}
