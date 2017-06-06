package pathfinding;

import graph.Edge;
import graph.Graph;
import graph.Node;

import java.util.ArrayList;
import java.util.List;

public class BFSPathFinder extends PathFinder {
    private Graph graph;

    private List<Node> queue = new ArrayList<>();

    private List<Edge> path = new ArrayList<>();
    private int pathLength;
    private List<Node> traversedNodes = new ArrayList<>();

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
    public void traverse(Node sourceNode) {
        queue = new ArrayList<>();

        enqueue(sourceNode);
        sourceNode.setDistanceFromSource(0);
        sourceNode.setMarked(true);
        traversedNodes.add(sourceNode);

        while (!isQueueEmpty()) {
            Node currentNode = popQueue();

            List<Edge> edges = currentNode.getEdges();

            for (Edge edge: edges) {
                Node neighbor = edge.getNodeTo();
                if (!neighbor.isMarked()) {
                    // We don't use the weight of the edge, since we are working
                    // with an unweighted graph, the
                    int distance = currentNode.getDistanceFromSource() + 1;
                    neighbor.setDistanceFromSource(distance);

                    neighbor.setPredecessor(currentNode);

                    enqueue(neighbor);
                    neighbor.setMarked(true);
                    traversedNodes.add(neighbor);
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

    @Override
    public List<Node> getTraversedNodes() {
        return traversedNodes;
    }

    @Override
    public void clearTraversedNodes() {
        traversedNodes = new ArrayList<>();
    }
}
