package pathfinding;

import graph.Edge;
import graph.Graph;
import graph.Node;

import java.util.*;

public class DijkstraPathfinder extends PathFinder {
    private Graph graph;

    private List<Edge> path = new ArrayList<>();
    private int pathLength;

    public DijkstraPathfinder(Graph graph) {
        this.graph = graph;
    }

    // TODO throw if called before calling computeShortestPath
    @Override
    public List<Edge> getPath() {
        return path;
    }

    // TODO throw if called before calling computeShortestPath
    @Override
    public int getPathLength() {
        return pathLength;
    }

    @Override
    Graph getGraph() {
        return graph;
    }

    @Override
    public void computeShortestPath(String departure, String arrival) {
        Node sourceNode = graph.findNodeByName(departure);
        Node targetNode = graph.findNodeByName(arrival);

        computeShortestPath(sourceNode, targetNode);
    }

    public void computeShortestPath(Node sourceNode, Node targetNode) {
        // TODO check every weight is positive ?
        int pathLength = 0;

        runTraversal(sourceNode);

        pathLength = targetNode.getDistanceFromSource();

        if (pathLength == Integer.MAX_VALUE) {
            System.out.println(sourceNode);
            System.out.println(targetNode);
            throw new PathFindingException("No path exists between source and target node !");
        }

        // Build the successive path steps, going backwards
        // (because each node has a reference towards its predecessor)
        List<Edge> pathSteps = new ArrayList<>();
        Node currentNode = targetNode;

        Node predecessor = targetNode.getPredecessor();

        Edge edgeToHere = predecessor.getEdgeToNeighbor(currentNode);
        pathSteps.add(edgeToHere);

        currentNode = predecessor;

        while (! currentNode.equals(sourceNode)) {
            predecessor = currentNode.getPredecessor();
            edgeToHere = predecessor.getEdgeToNeighbor(currentNode);
            pathSteps.add(edgeToHere);

            currentNode = predecessor;
        }

        Collections.reverse(pathSteps);
        this.path = pathSteps;
        this.pathLength = pathLength;
    }

    private boolean areAllMarked() {
        Map<Integer, Node> nodes = graph.getNodes();

        boolean b = true;
        for (int currentID : nodes.keySet()) {
            if (!nodes.get(currentID).isMarked()) {
                b = false;
            }
        }
        return b;
    }

    public void runTraversal(Node sourceNode) {
        resetBeforeTraversal();

        Node currentNode = sourceNode;
        currentNode.setDistanceFromSource(0);

        while (currentNode != null) {
            for (Node neighbor : currentNode.getNeighbors()) {
                if (currentNode.getDistanceFromSource() + currentNode.getWeightForNeigbhor(neighbor) < neighbor.getDistanceFromSource()) {
                    neighbor.setDistanceFromSource(currentNode.getDistanceFromSource() + currentNode.getWeightForNeigbhor(neighbor));
                    neighbor.setPredecessor(currentNode);
                }
            }
            currentNode.setMarked(true);

            // Find the new current node
            currentNode = findUnmarkedNodeAtMinimalDistance();
        }
    }

    // TODO a better way to do this could be to keep up-to-date a Map that would map distances of unmarked Nodes to these nodes (then the number of Nodes to iterate on would decrease as the algorithm runs)
    private Node findUnmarkedNodeAtMinimalDistance() {
        Map<Integer, Node> nodes = graph.getNodes();
        Node selectedNode = null;

        for (Map.Entry<Integer, Node> currentEntry : nodes.entrySet()) {
            Node currentNode = currentEntry.getValue();

            // Keep only unmarked nodes :
            if (currentNode.isMarked()) continue;
            if (selectedNode == null) {
                selectedNode = currentNode;
                continue;
            }

            if (currentNode.getDistanceFromSource() < selectedNode.getDistanceFromSource()) {
                selectedNode = currentNode;
            }
        }

        return selectedNode;
    }
}
