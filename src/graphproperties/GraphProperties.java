package graphproperties;

import graph.Edge;
import graph.Graph;
import graph.Node;
import pathfinding.PathFinder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class GraphProperties {
    private PathFinder pathFinder;
    private Graph graph;

    public Integer getRadius() {
        return radius;
    }

    private Integer radius;
    private Integer diameter;

    private List<Edge> diameterPath;
    private int diameterPathLength;

    public GraphProperties(PathFinder pathFinder, Graph graph) {
        this.graph = graph;
        this.pathFinder = pathFinder;
    }

    public void computeRadiusAndDiameter() {
        Map<Integer, Node> nodes = graph.getNodes();

        for (Map.Entry<Integer, Node> currentEntry : nodes.entrySet()) {
            Node currentNode = currentEntry.getValue();

            pathFinder.resetNodesProperties(); // TODO necessary ?
            pathFinder.traverse(currentNode);

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

                // TODO what if eccentricity == Integer.MAX_VALUE ?

                // Handle computation of the longest path and of the radius and diameter :
                if (diameter == null || eccentricity > diameter) {
                    diameter = eccentricity;

                    // If we have found a new longest path (aka a new value for the diameter of the graph),
                    // we compute the actual path so that we can brag about it elsewhere :
                    pathFinder.computeShortestPathWithoutTraversing(currentNode, maxEntry.getValue());

                    diameterPath = pathFinder.getPath();
                    diameterPathLength = pathFinder.getPathLength();
                }
                if (radius == null || eccentricity < radius) {
                    radius = eccentricity;
                }
            }
        }
    }

    public void resetEdgeBetweenness() {
        List<Edge> edges = graph.getEdges();
        edges.forEach(edge -> edge.setBetweenness(0));
    }

    public void computeEdgeBetweenness() {
        resetEdgeBetweenness();
        Map<Integer, Node> nodes = graph.getNodes();

        for (Map.Entry<Integer, Node> currentEntry : nodes.entrySet()) {
            Node currentNode = currentEntry.getValue();

            pathFinder.resetNodesProperties();
            pathFinder.traverse(currentNode);

            // Compute the edges betweenness :
            for (Map.Entry<Integer, Node> entry : nodes.entrySet()) {
                Node node = entry.getValue();

                // pathFinder.hasPathTo(node) : prevent counting non-existent paths
                // node != currentNode : prevent counting the path from currentNode to currentNode
                if (pathFinder.hasPathTo(node) && node != currentNode) {
                    pathFinder.computeShortestPathWithoutTraversing(currentNode, node);

                    List<Edge> path = pathFinder.getPath();

                    for (Edge edge : path) {
                        edge.setBetweenness(edge.getBetweenness() + 1);
                    }
                }
            }
        }
    }

    public List<List<Node>> connectedComponents() {
        List<List<Node>> connectedComponents = new ArrayList<>();

        pathFinder.resetNodesProperties();

        for (Map.Entry<Integer, Node> nodeEntry: graph.getNodes().entrySet()) {
            Node node = nodeEntry.getValue();
            if (!node.isMarked()) {
                pathFinder.clearTraversedNodes();
                pathFinder.traverse(node);

                List<Node> markedNodes = pathFinder.getTraversedNodes();
                connectedComponents.add(markedNodes);
            }
        }

        return connectedComponents;
    }

    public List<Edge> getDiameterPath() {
        return diameterPath;
    }

    public int getDiameterPathLength() {
        return diameterPathLength;
    }

    public void girvanNewman() {
        resetEdgeBetweenness();
        computeEdgeBetweenness();

        int targetCC = 10; // Target number of connected components in the graph
        int iterations = 0;

        while (connectedComponents().size() < targetCC) {
            int currentCC = connectedComponents().size();
            iterations ++;

            if (graph.getEdges().size() == 0) {
                System.out.println("No edges remaining. Stopping the algorithm");
                return;
            }

            // Find maximal betweenness :
            int max = -1;
            for (Edge e : graph.getEdges()) {
                if (e.getBetweenness() > max) {
                    max = e.getBetweenness();
                }
            }

            // Find all edges we have to remove :
            // We have to prepare this list before actual removal, or else we get a ConcurrentModificationException
            // (modifying a list while iterating over its elements)
            List<Edge> edgesToRemove = new ArrayList<>();
            for (Edge e: graph.getEdges()) {
                if (e.getBetweenness() == max) {
                    edgesToRemove.add(e);
                }
            }

            System.out.println("Removing : " + String.join(", ", edgesToRemove.stream().map(e -> e.getNodeFrom().getName() + " / " + e.getNodeTo().getName()).collect(Collectors.toList())));
            for (Edge e: edgesToRemove) {
                graph.removeEdge(e);
            }

            // Recompute edge betweenness, taking into account the removal of some Edges :
            computeEdgeBetweenness();

            // If we have made a cluster appear by disconnecting it, describe it via a little console output :
            if (connectedComponents().size() != currentCC) {
                System.out.println();
                System.out.println("Number of iterations : " + iterations);
                System.out.println("Number of clusters : " + connectedComponents().size());
                for (List<Node> nodes : connectedComponents()) {
                    System.out.println("* " + String.join(", ", nodes.stream().map(n -> n.getName()).collect(Collectors.toList())));
                    System.out.println(nodes.size());
                }
            }
        }
    }
}
