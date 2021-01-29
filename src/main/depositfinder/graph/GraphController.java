package main.depositfinder.graph;

import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class GraphController {
    private final Map<LocationNode, Deque<LocationNode>> routesToNearestDeposits;
    private final Map<LocationNode, Integer> timesToNearestDeposits;
    private final DepositLocationGraph depositLocationGraph;

    public GraphController() {
        routesToNearestDeposits = new HashMap<>();
        timesToNearestDeposits = new HashMap<>();
        depositLocationGraph = new DepositLocationGraph();
    }

    public void defineAndAddEdge(String[] edgeInfo) throws IllegalArgumentException {
        TravelTime travelTime;
        travelTime = new TravelTime(edgeInfo[2]);

        Optional<LocationNode> node1 = depositLocationGraph.getNode(edgeInfo[0]);
        Optional<LocationNode> node2 = depositLocationGraph.getNode(edgeInfo[1]);
        if (node1.isEmpty()) {
            if (DepositLocation.fromString(edgeInfo[0]) != null) {
                node1 = Optional.of(new DepositLocationNode(edgeInfo[0]));
            } else {
                node1 = Optional.of(new LocationNode(edgeInfo[0]));
            }
            depositLocationGraph.addNode(node1.get());
        }

        if (node2.isEmpty()) {
            if (DepositLocation.fromString(edgeInfo[1]) != null) {
                node2 = Optional.of(new DepositLocationNode(edgeInfo[1]));
            } else {
                node2 = Optional.of(new LocationNode(edgeInfo[1]));
            }
            depositLocationGraph.addNode(node2.get());
        }
        node1.get().addAdjacentLocation(node2.get(), travelTime.get());
        node2.get().addAdjacentLocation(node1.get(), travelTime.get());
    }

    public int getTravelTimeToNearestDepositLocation(LocationNode origin) {
        if (!timesToNearestDeposits.containsKey(origin)) {
            findRouteToNearestDepositLocation(origin);
        }
        return timesToNearestDeposits.get(origin);
    }

    public Deque<LocationNode> getRouteToNearestDepositLocation(LocationNode origin) {
        if (!routesToNearestDeposits.containsKey(origin)) {
            findRouteToNearestDepositLocation(origin);
        }
        return routesToNearestDeposits.get(origin);
    }

    public void outputNearestRouteForUser(String[] userInfo) {
        Optional<LocationNode> userLocation = depositLocationGraph.getNode(userInfo[1]);
        if (userLocation.isPresent()) {
            Deque<LocationNode> route = getRouteToNearestDepositLocation(userLocation.get());
            StringBuilder builder = new StringBuilder(userInfo[0]);
            while (!route.isEmpty()) {
                builder.append(' ')
                        .append(route.pop().getName());
            }
            int travelTime = getTravelTimeToNearestDepositLocation(userLocation.get());
            if (Integer.MAX_VALUE == travelTime) {
                builder.append(" has no way of reaching a deposit location.");
            } else {
                builder.append(' ')
                        .append(getTravelTimeToNearestDepositLocation(userLocation.get()));
            }
            System.out.println(builder.toString());
        } else {
            System.out.println("Error: \"" + userInfo[1] + "\" is not a known location. (ignoring)");
        }
    }


    /**
     *  Finds the minimum travel time from origin node to any deposit location
     *  and stores the route and travel time in @GraphController maps. Uses Dijkstra's
     *  shortest path algorithm until it finds a deposit location
     *  and no shorter paths exists.
     * @param origin the origin location to find route from
     */
    private void findRouteToNearestDepositLocation(LocationNode origin) {
        List<LocationNode> markedNodes = new ArrayList<>();
        Map<LocationNode, LocationNode> nodeParents = new HashMap<>();
        Map<LocationNode, Integer> shortestTimesMap = new HashMap<>();
        Map<LocationNode, Integer> closestNodeNeighbors;
        Deque<LocationNode> route = new LinkedList<>();
        LocationNode closestUnmarkedNode = origin;
        int pathToNode;
        int timeToClosestNode = 0;
        int timeToDepositLocation = Integer.MAX_VALUE;
        boolean depositLocationFound = false;

        for (LocationNode location : depositLocationGraph.getLocations()) {
            shortestTimesMap.put(location, Integer.MAX_VALUE);
        }

        shortestTimesMap.put(origin, 0);
        nodeParents.put(origin, null);

        while (closestUnmarkedNode != null
                && (!depositLocationFound
                || timeToClosestNode < timeToDepositLocation)) {
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

            // pick closest location that has not been examined yet
            closestUnmarkedNode = getClosestUnmarkedNode(shortestTimesMap, markedNodes);
            if (closestUnmarkedNode != null) {
                timeToClosestNode = shortestTimesMap.get(closestUnmarkedNode);
            }
        }

        // build route
        if (!route.isEmpty()) {
            LocationNode parentNode = nodeParents.get(route.getFirst());
            while (parentNode != null) {
                route.addFirst(parentNode);
                parentNode = nodeParents.get(parentNode);
            }
        }

        routesToNearestDeposits.put(origin, route);
        timesToNearestDeposits.put(origin, timeToDepositLocation);
    }

    private LocationNode getClosestUnmarkedNode(Map<LocationNode, Integer> map, List<LocationNode> markedNodes) {
        Map.Entry<LocationNode, Integer> min = null;
        for (Map.Entry<LocationNode, Integer> entry : map.entrySet()) {
            if ((min == null) || min.getValue() > entry.getValue()) {
                if (!markedNodes.contains(entry.getKey()) && !entry.getValue().equals(Integer.MAX_VALUE)) {
                    min = entry;
                }
            }
        }
        return min == null ? null : min.getKey();
    }
}
