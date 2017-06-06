package graphproperties;

import graph.Edge;
import graph.Graph;
import graph.Node;
import pathfinding.PathFinder;

import java.util.List;
import java.util.Map;

public abstract class Diameter {
    public void computeGraphProperties() {
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
                if (getDiameter() == null || eccentricity > getDiameter()) {
                    setDiameter(eccentricity);

                    // If we have found a new longest path (aka a new value for the diameter of the graph),
                    // we compute the actual path so that we can brag about it elsewhere :
                    pathFinder.computeShortestPath(currentNode, maxEntry.getValue());

                    setLongestShortestPath(pathFinder.getPath());
                    setLongestShortestPathLength(pathFinder.getPathLength());
                }
                if (getRadius() == null || eccentricity < getRadius()) {
                    setRadius(eccentricity);
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
