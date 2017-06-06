package pathfinding;

import graph.Edge;
import graph.Graph;
import graph.Node;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public abstract class PathFinder {
    public void computeShortestPath(String departure, String arrival) {
        Node sourceNode = getGraph().findNodeByName(departure);
        Node targetNode = getGraph().findNodeByName(arrival);

        computeShortestPath(sourceNode, targetNode);
    }

    public void computeShortestPath(Node departure, Node arrival) {
        performChecks();

        runTraversal(departure);

        computeShortestPathWithoutRunningTraversal(departure, arrival);
    }

    public void computeShortestPathWithoutRunningTraversal(Node departure, Node arrival) {
        int pathLength = arrival.getDistanceFromSource();

        if (pathLength == Integer.MAX_VALUE) {
            throw new PathFindingException("No path exists between source (" + departure.getName() + ") and target (" + arrival.getName() + ") node !");
        }

        // Build the successive path steps, going backwards
        // (because each node has a reference towards its predecessor)
        List<Edge> pathSteps = new ArrayList<>();
        Node currentNode = arrival;

        Node predecessor = arrival.getPredecessor();

        Edge edgeToHere = predecessor.getEdgeToNeighbor(currentNode);
        pathSteps.add(edgeToHere);

        currentNode = predecessor;

        while (!currentNode.equals(departure)) {
            predecessor = currentNode.getPredecessor();
            edgeToHere = predecessor.getEdgeToNeighbor(currentNode);
            pathSteps.add(edgeToHere);

            currentNode = predecessor;
        }

        Collections.reverse(pathSteps);

        this.setPath(pathSteps);
        this.setPathLength(pathLength);
    }

    public boolean hasPathTo(Node node) {
        return node.getDistanceFromSource() < Integer.MAX_VALUE;
    }

    abstract public List<Edge> getPath();

    abstract public int getPathLength();

    abstract Graph getGraph();

    abstract public void runTraversal(Node sourceNode);

    void resetBeforeTraversal() {
        Map<Integer, Node> nodes = getGraph().getNodes();

        nodes.forEach((id, node) -> {
            node.setMarked(false);
            node.setDistanceFromSource(Integer.MAX_VALUE);
            node.setPredecessor(null);
        });
    }

    // TODO make this method abstract and override it to perform required checks inside the implementations
    abstract public void performChecks();

    abstract public void setPath(List<Edge> path);

    abstract public void setPathLength(int pathLength);
}
