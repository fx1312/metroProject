package pathfinding;

import graph.Edge;
import graph.Graph;
import graph.Node;

import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;

public class BFSPathFinder extends PathFinder {
    private Graph graph;

    private List<Node> queue = new ArrayList<>();

    private List<Edge> path = new ArrayList<>();
    private int pathLength;

    public BFSPathFinder(Graph graph) {
        this.graph = graph;
    }

    private Node popQueue() {
        Node n = queue.get(0);
        queue.remove(0);
        return n;
    }

    private void enqueue(Node node) {
        queue.add(node);
    }

    private boolean isQueueEmpty() {
        return queue.size() == 0;
    }

    @Override
    public void runTraversal(Node sourceNode) {
        resetBeforeTraversal();
        queue = new ArrayList<>();

        enqueue(sourceNode);
        sourceNode.setDistanceFromSource(0);
        sourceNode.setMarked(true);

        while (!isQueueEmpty()) {
            Node currentNode = popQueue();

            List<Node> neighbors = currentNode.getNeighbors();

            for (Node neighbor: neighbors) {
                if (!neighbor.isMarked()) {
                    // We don't use the weight of the edge, since we are working
                    // with an unweighted graph, the
                    int distance = currentNode.getDistanceFromSource() + 1;
                    neighbor.setDistanceFromSource(distance);

                    neighbor.setPredecessor(currentNode);

                    enqueue(neighbor);
                    neighbor.setMarked(true);
                }
            }
        }
    }

    @Override
    public void performChecks() {
        // TODO what to do here ?
    }

    @Override
    public List<Edge> getPath() {
        return path;
    }

    @Override
    public int getPathLength() {
        return pathLength;
    }

    @Override
    Graph getGraph() {
        return graph;
    }

    @Override
    public void setPath(List<Edge> path) {
        this.path = path;
    }

    @Override
    public void setPathLength(int pathLength) {
        this.pathLength = pathLength;
    }
}
