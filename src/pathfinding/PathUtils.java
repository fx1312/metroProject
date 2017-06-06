package pathfinding;

import graph.Edge;
import graph.EdgeType;

import java.util.List;

public class PathUtils {
    public static void printPath(List<Edge> path, int pathLength, String unit) {
        System.out.format("Longueur du trajet : %s %s", pathLength, unit);
        System.out.println(); // TODO system.lineSeparator

        for (Edge edge: path) {
            String lineDescription;
            if (edge.getType() == EdgeType.METRO_LINE) {
                lineDescription = "Ligne " + edge.getLine();
            } else {
                lineDescription = "Correspondance à pied";
            }
            System.out.format(
                    "* %s entre %s et %s (distance : %s mètres)",
                    lineDescription,
                    edge.getNodeFrom().getName(),
                    edge.getNodeTo().getName(),
                    edge.getWeight()
            );
            System.out.println();
        }
    }

    public static void printPath(List<Edge> path, int pathLength) {
        printPath(path, pathLength, "mètre(s)");
    }
}
