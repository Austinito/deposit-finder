package ah.depositfinder;

import ah.depositfinder.graph.DepositLocation;
import ah.depositfinder.graph.DepositLocationGraph;
import ah.depositfinder.graph.DepositLocationNode;
import ah.depositfinder.graph.Edge;
import ah.depositfinder.graph.LocationNode;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Deque;
import java.util.Optional;
import java.util.Scanner;

public class Main {
    private static final int EDGE_DEFINITION_NUM_WORDS = 3;
    private static final int USER_DEFINITION_NUM_WORDS = 2;

    public static void main(String[] args) {
        if (args.length != 1) {
            System.out.println("Usage: java DepositFinder <input file>");
            System.exit(0); // end app.
        }

        try {
            File inputFile = new File(args[0]);
            Scanner scanner = new Scanner(inputFile);

            int lineNum = 0;
            String inputLine;
            String[] lineWords;
            DepositLocationGraph depositLocationGraph = new DepositLocationGraph();

            while (scanner.hasNextLine()) {
                inputLine = scanner.nextLine();
                lineWords = inputLine.split(" ");
                lineNum++;
                if (lineWords.length == EDGE_DEFINITION_NUM_WORDS) {
                    try {
                        defineAndAddEdge(lineWords, depositLocationGraph);
                    } catch (NumberFormatException e) {
                        System.out.println("Error: Could not parse travel time on line " + lineNum + " (skipped): " + inputLine);
                    }
                } else if (lineWords.length == USER_DEFINITION_NUM_WORDS) {
                    findNearestRouteForUser(lineWords, depositLocationGraph);
                } else {
                    System.out.println("Error: Could not parse line " + lineNum + " as edge (skipped): " + inputLine);
                }
            }
//            depositLocationGraph.print();
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found \"" + args[0] + '"');
            System.exit(0); // end app.
        }


    }

    private static void findNearestRouteForUser(String[] userInfo, DepositLocationGraph depositLocationGraph) {
        Optional<LocationNode> userLocation = depositLocationGraph.getNode(userInfo[1]);
        if (userLocation.isPresent()) {
            Deque<LocationNode> route = depositLocationGraph.getRouteToNearestDepositLocation(userLocation.get());
            StringBuilder builder = new StringBuilder(userInfo[0]);
            while (!route.isEmpty()) {
                builder.append(' ')
                        .append(route.pop().getName());
            }
            builder.append(' ')
                    .append(depositLocationGraph.getTravelTimeToNearestDepositLocation(userLocation.get()));
            System.out.println(builder.toString());
        } else {
            System.out.println("Error: \"" + userInfo[1] + "\" is not a known location. (ignoring)");
        }
    }

    private static void defineAndAddEdge(String[] edgeInfo, DepositLocationGraph depositLocationGraph) throws NumberFormatException {
        Optional<LocationNode> node1 = depositLocationGraph.getNode(edgeInfo[0]);
        Optional<LocationNode> node2 = depositLocationGraph.getNode(edgeInfo[1]);
        int travelTime;
        if (!node1.isPresent()) {
            if (DepositLocation.fromString(edgeInfo[0]) != null) {
                node1 = Optional.of(new DepositLocationNode(edgeInfo[0]));
            } else {
                node1 = Optional.of(new LocationNode(edgeInfo[0]));
            }
            depositLocationGraph.addNode(node1.get());
        }

        if (!node2.isPresent()) {
            if (DepositLocation.fromString(edgeInfo[1]) != null) {
                node2 = Optional.of(new DepositLocationNode(edgeInfo[1]));
            } else {
                node2 = Optional.of(new LocationNode(edgeInfo[1]));
            }
            depositLocationGraph.addNode(node2.get());
        }
        travelTime = Integer.parseInt(edgeInfo[2]);

        depositLocationGraph.addEdge(new Edge(node1.get(), node2.get(), travelTime));
    }
}

