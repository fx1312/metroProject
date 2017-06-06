package graphproperties;

import graph.Edge;
import graph.Graph;
import pathfinding.DijkstraPathfinder;
import pathfinding.PathFinder;

import java.util.List;

public class DijkstraDiameter extends Diameter {
    private DijkstraPathfinder dijkstraPathfinder;
    private Graph graph;

    private Integer radius;
    private Integer diameter;

    private List<Edge> longestShortestPath;
    private int longestShortestPathLength;

    public DijkstraDiameter(Graph graph) {
        this.graph = graph;
        dijkstraPathfinder = new DijkstraPathfinder(graph);
    }

    @Override
    protected PathFinder getPathFinder() {
        return dijkstraPathfinder;
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
    public List<Edge> getLongestShortestPath() {
        return longestShortestPath;
    }

    // TODO throw if this is called before calling computeGraphProperties :
    @Override
    public Integer getLongestShortestPathLength() {
        return longestShortestPathLength;
    }
}
