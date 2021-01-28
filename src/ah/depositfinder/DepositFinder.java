package ah.depositfinder;

import ah.depositfinder.graph.GraphController;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class DepositFinder {
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
            GraphController graphController = new GraphController();

            while (scanner.hasNextLine()) {
                inputLine = scanner.nextLine();
                lineWords = inputLine.split(" ");
                lineNum++;
                if (lineWords.length == EDGE_DEFINITION_NUM_WORDS) {
                    try {
                        graphController.defineAndAddEdge(lineWords);
                    } catch (NumberFormatException e) {
                        System.out.println("Error: Could not parse travel time on line " + lineNum + " (skipped): " + inputLine);
                    } catch (IllegalArgumentException e) {
                        System.out.println("Error: " + e.getMessage() + " Line " + lineNum + " (skipped): " + inputLine);
                    }
                } else if (lineWords.length == USER_DEFINITION_NUM_WORDS) {
                    graphController.outputNearestRouteForUser(lineWords);
                } else {
                    System.out.println("Error: Could not parse line " + lineNum + " (skipped): " + inputLine);
                }
            }
            scanner.close();
        } catch (FileNotFoundException e) {
            System.out.println("Error: File not found \"" + args[0] + '"');
            System.exit(0); // end app.
        }
    }
}
