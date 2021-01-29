package DepositFinder;

import DepositFinder.graph.GraphController;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * DepositFinder is a console application that will parse the input of a file and return a path for each user
 * to the nearest deposit location and the total travel time to reach that destination. We accept a file
 * that will first define a bi-directional graph in which the nodes are waypoints or deposit locations, and
 * the edges are travel times. Deposit Locations are represented with "SB", "7E", "PTS", "CVS" or "FD". Afterwards,
 * the next section of input consists of app usernames followed by their current location.
 * <p>
 * The program's output will print username followed by the route and total travel time.
 *
 * @author Austin Herrera
 */
public class DepositFinder {
    private static final int EDGE_DEFINITION_NUM_WORDS = 3;
    private static final int USER_DEFINITION_NUM_WORDS = 2;
    private static final Logger LOGGER = Logger.getLogger(DepositFinder.class.getName());

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
            GraphController graphController = new GraphController();

            while (scanner.hasNextLine()) {
                inputLine = scanner.nextLine();
                lineWords = inputLine.split(" ");
                lineNum++;
                if (lineWords.length == EDGE_DEFINITION_NUM_WORDS) {
                    try {
                        graphController.defineAndAddEdge(lineWords);
                    } catch (IllegalArgumentException e) {
                        LOGGER.log(Level.WARNING, "Line " + lineNum + ": " + e.getMessage());
                    }
                } else if (lineWords.length == USER_DEFINITION_NUM_WORDS) {
                    graphController.outputNearestRouteForUser(lineWords);
                } else {
                    LOGGER.log(Level.WARNING, "Line " + lineNum + ": Incorrect amount of arguments.");
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            LOGGER.log(Level.SEVERE, "File not found \"" + args[0] + "\". Ending.");
            System.exit(0); // end app.
        }
    }
}

