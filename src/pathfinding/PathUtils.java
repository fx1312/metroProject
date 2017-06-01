package pathfinding;

import graph.Edge;

import java.util.List;

public class PathUtils {
    public static void printPath(List<Edge> path, int pathLength) {
        System.out.format("Longueur du trajet : %s", pathLength);
        System.out.println(); // TODO system.lineSeparator

        for (Edge edge: path) {
            System.out.format("* Ligne %s entre %s et %s", edge.getLine(), edge.getNodeFrom().getName(), edge.getNodeTo().getName());
            System.out.println();
        }
    }
}
