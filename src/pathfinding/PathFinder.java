package pathfinding;

import graph.Edge;
import graph.Graph;
import graph.Node;

import java.util.List;
import java.util.Map;

public abstract class PathFinder {
    abstract public void computeShortestPath(String departure, String arrival);

    abstract public List<Edge> getPath();

    abstract public int getPathLength();

    abstract Graph getGraph();

    protected void resetBeforeTraversal() {
        Map<Integer, Node> nodes = getGraph().getNodes();

        nodes.forEach((id, node) -> {
            node.setMarked(false);
            node.setDistanceFromSource(Integer.MAX_VALUE);
            node.setPredecessorId(-1);
        });
    }
}
