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
        int sourceId = graph.nameToId(departure);

        Node sourceNode = graph.findNodeByName(departure);
        Node targetNode = graph.findNodeByName(arrival);

        // TODO check every weight is positive ?

        int pathLength = 0;

        runTraversal(sourceId);

        pathLength = targetNode.getDistanceFromSource();

        if (pathLength == Integer.MAX_VALUE) {
            throw new PathFindingException("No path exists between source and target node !");
        }

        System.out.println(pathLength);

        List<Edge> pathSteps = new ArrayList<>();
        Node currentNode = targetNode;

        int predecessorId = targetNode.getPredecessorId();
        Node predecessor = graph.findNodeById(predecessorId);
        Edge edgeToHere = predecessor.getEdgeToNeighbor(currentNode);
        pathSteps.add(edgeToHere);

        currentNode = predecessor;


        while (! currentNode.equals(sourceNode)) {
            predecessorId = currentNode.getPredecessorId();
            predecessor = graph.findNodeById(predecessorId);
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

    private void runTraversal(int sourceNodeId) {
        Map<Integer, Node> nodes = graph.getNodes();

        resetBeforeTraversal();

        int currentNodeId = sourceNodeId;
        nodes.get(sourceNodeId).setDistanceFromSource(0);

        while (currentNodeId != -1) {
            Node currentNode = nodes.get(currentNodeId);
            for (Node neighbor : currentNode.getNeighbors()) {
                if (currentNode.getDistanceFromSource() + currentNode.getWeightForNeigbhor(neighbor) < neighbor.getDistanceFromSource()) {
                    neighbor.setDistanceFromSource(currentNode.getDistanceFromSource() + currentNode.getWeightForNeigbhor(neighbor));
                    neighbor.setPredecessorId(currentNodeId);
                }
            }
            currentNode.setMarked(true);

            // Find the new current node
            currentNodeId = selectNodeForDijkstra();
        }
    }

    private int selectNodeForDijkstra() {
        Map<Integer, Node> nodes = graph.getNodes();

        List<Node> sortedNodes = new ArrayList<>();
        if (areAllMarked()) {
            return -1;
        }

        for (int currentNodeIndex : nodes.keySet()) {
            Node currentNode = nodes.get(currentNodeIndex);
            if (!currentNode.isMarked()) {
                if (sortedNodes.size() == 0) {
                    sortedNodes.add(currentNode);
                } else {
                    if (currentNode.getDistanceFromSource() <= sortedNodes.get(0).getDistanceFromSource()) {
                        sortedNodes.add(0, currentNode);
                    }
                    if (currentNode.getDistanceFromSource() > sortedNodes.get(0).getDistanceFromSource()) {
                        sortedNodes.add(sortedNodes.size() - 1, currentNode);
                    }
                }
            }
        }
        return (sortedNodes.get(0).getId());
    }
}
