package graphproperties;

import graph.Edge;
import graph.Graph;
import graph.Node;
import pathfinding.DijkstraPathfinder;

import java.util.List;
import java.util.Map;

public class DijkstraDiameter {
    private DijkstraPathfinder dijkstraPathfinder;
    private Graph graph;

    private Integer radius;
    private Integer diameter;

    private List<Edge> longestShortestPath;
    private int longestShortestPathLength;

    public DijkstraDiameter(Graph graph) {
        this.graph = graph;
        dijkstraPathfinder = new DijkstraPathfinder(graph);
    }

    public void computeGraphProperties() {
        Map<Integer, Node> nodes = graph.getNodes();

        for (Map.Entry<Integer, Node> currentEntry : nodes.entrySet()) {
            Node currentNode = currentEntry.getValue();
            dijkstraPathfinder.runTraversal(currentNode);

            // Find the node that has the maximum distance with the current Node
            // The distance between the current node and the farthest Node is the current Node's eccentricity
            Map.Entry<Integer, Node> maxEntry = null;

            for (Map.Entry<Integer, Node> entry : nodes.entrySet()) {
                if (maxEntry == null || entry.getValue().getDistanceFromSource() > maxEntry.getValue().getDistanceFromSource()) {
                    maxEntry = entry;
                }
            }

            // If there is an eccentricity for the current node, update radius and diameter :
            if (maxEntry != null) { // Guard
                int eccentricity = maxEntry.getValue().getDistanceFromSource();
                currentNode.setEccentricity(eccentricity);

                // TODO throw away this, but find a thing to do when we encouter this case (throw an exception probably)
                if (eccentricity == Integer.MAX_VALUE) {
                    System.out.println("**** problème ****");
                    System.out.println(currentNode.getName());
                    System.out.println(currentNode.getLines());
                    System.out.println(maxEntry.getValue().getName());
                    System.out.println(maxEntry.getValue().getLines());
                    continue;
                }

                // Handle computation of the longest path and of the radius and diameter :
                if (diameter == null || eccentricity > diameter) {
                    diameter = eccentricity;

                    // If we have found a new longest path (aka a new value for the diameter of the graph),
                    // we compute the actual path so that we can brag about it elsewhere :
                    dijkstraPathfinder.computeShortestPath(currentNode, maxEntry.getValue());
                    longestShortestPath = dijkstraPathfinder.getPath();
                    longestShortestPathLength = dijkstraPathfinder.getPathLength();
                }
                if (radius == null || eccentricity < radius) {
                    radius = eccentricity;
                }
            }
        }
    }

    // TODO throw if this is called before calling computeGraphProperties :
    public List<Edge> getLongestShortestPath() {
        return longestShortestPath;
    }

    // TODO throw if this is called before calling computeGraphProperties :
    public int getLongestShortestPathLength() {
        return longestShortestPathLength;
    }
}
