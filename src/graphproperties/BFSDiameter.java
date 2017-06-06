package graphproperties;

import graph.Edge;
import graph.Graph;
import pathfinding.BFSPathFinder;
import pathfinding.PathFinder;

import java.util.List;

public class BFSDiameter extends Diameter {
    private BFSPathFinder bfsPathFinder;
    private Graph graph;

    private Integer radius;
    private Integer diameter;

    private List<Edge> longestShortestPath;
    private int longestShortestPathLength;

    public BFSDiameter(Graph graph) {
        this.graph = graph;
        bfsPathFinder = new BFSPathFinder(graph);
    }

    @Override
    protected PathFinder getPathFinder() {
        return bfsPathFinder;
    }

    @Override
    protected Graph getGraph() {
        return graph;
    }

    @Override
    protected Integer getDiameter() {
        return diameter;
    }

    @Override
    protected void setDiameter(int diameter) {
        this.diameter = diameter;
    }

    @Override
    protected Integer getRadius() {
        return radius;
    }

    @Override
    protected void setRadius(int radius) {
        this.radius = radius;
    }

    // TODO throw if this is called before calling computeGraphProperties :
    @Override
    public List<Edge> getLongestShortestPath() {
        return longestShortestPath;
    }

    @Override
    protected void setLongestShortestPath(List<Edge> longestShortestPath) {
        this.longestShortestPath = longestShortestPath;
    }

    @Override
    protected void setLongestShortestPathLength(int length) {
        this.longestShortestPathLength = length;
    }

    // TODO throw if this is called before calling computeGraphProperties :
    @Override
    public Integer getLongestShortestPathLength() {
        return longestShortestPathLength;
    }
}
