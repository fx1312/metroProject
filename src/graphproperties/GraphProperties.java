package graphproperties;

import graph.Edge;
import graph.Graph;
import graph.Node;
import pathfinding.PathFinder;

import java.util.List;
import java.util.Map;


// TODO not abstract (the only difference between implementations is the PathFinder that gets passed ==> construct with a pathfinder isntance as constructor parameter, and there is no need for this class to be abstract)
public abstract class GraphProperties {
    public void computeRadiusAndDiameter() {
        PathFinder pathFinder = getPathFinder();
        Map<Integer, Node> nodes = getGraph().getNodes();

        for (Map.Entry<Integer, Node> currentEntry : nodes.entrySet()) {
            Node currentNode = currentEntry.getValue();
            pathFinder.runTraversal(currentNode);

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
                if (getDiameter() == null || eccentricity > getDiameter()) {
                    setDiameter(eccentricity);

                    // If we have found a new longest path (aka a new value for the diameter of the graph),
                    // we compute the actual path so that we can brag about it elsewhere :
                    pathFinder.computeShortestPathWithoutRunningTraversal(currentNode, maxEntry.getValue());

                    setLongestShortestPath(pathFinder.getPath());
                    setLongestShortestPathLength(pathFinder.getPathLength());
                }
                if (getRadius() == null || eccentricity < getRadius()) {
                    setRadius(eccentricity);
                }
            }
        }
    }

    public void resetEdgeBetweenness() {
        List<Edge> edges = getGraph().getEdges();
        edges.forEach(edge -> edge.setBetweenness(0));
    }

    public void computeEdgeBetweenness() {
        resetEdgeBetweenness();

        PathFinder pathFinder = getPathFinder();
        Map<Integer, Node> nodes = getGraph().getNodes();

        for (Map.Entry<Integer, Node> currentEntry : nodes.entrySet()) {
            Node currentNode = currentEntry.getValue();
            pathFinder.runTraversal(currentNode);

            // Compute the edges betweenness :
            for (Map.Entry<Integer, Node> entry : nodes.entrySet()) {
                Node node = entry.getValue();


                // pathFinder.hasPathTo(node) : prevent counting non-existent paths
                // node != currentNode : prevent counting the path from currentNode to currentNode
                if (pathFinder.hasPathTo(node) && node != currentNode) {

                    pathFinder.computeShortestPathWithoutRunningTraversal(currentNode, node);

                    List<Edge> path = pathFinder.getPath();

                    for (Edge edge : path) {
                        edge.setBetweenness(edge.getBetweenness() + 1);
                    }
                }
            }
        }
    }

    protected abstract PathFinder getPathFinder();

    protected abstract Graph getGraph();

    protected abstract Integer getDiameter();

    protected abstract void setDiameter(int diameter);

    protected abstract Integer getRadius();

    protected abstract void setRadius(int radius);

    protected abstract List<Edge> getLongestShortestPath();

    protected abstract void setLongestShortestPath(List<Edge> longestShortestPath);

    protected abstract void setLongestShortestPathLength(int length);

    protected abstract Integer getLongestShortestPathLength();
}
