package graph;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private int id;
    private String name;
    private List<Edge> edges = new ArrayList<>();
    private List<Integer> lines = new ArrayList<>();
    private List<Node> neighbors = new ArrayList<>();
    private double lng;
    private double lat;

    private int predecessorId = -1;
    private int distanceFromSource = Integer.MAX_VALUE; // TODO Integer.MAX_VALUE
    private boolean marked = false;

    Node(int id, String name, List<Integer> lines, double lng, double lat) {
        this.id = id;
        this.name = name;
        this.lines = lines;
        this.lat = lat;
        this.lng = lng;
    }

    public List<Edge> getEdges() {
        return edges;
    }

    public void setEdges(List<Edge> edges) {
        this.edges = edges;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Deprecated
    public int getId() {
        return id;
    }

    @Deprecated
    public void setId(int id) {
        this.id = id;
    }

    public List<Integer> getLines() {
        return lines;
    }

    public void setLines(List<Integer> lines) {
        this.lines = lines;
    }

    public List<Node> getNeighbors() {
        return neighbors;
    }

    public void setNeighbors(List<Node> neighbors) {
        this.neighbors = neighbors;
    }

    public int getPredecessorId() {
        return predecessorId;
    }

    public void setPredecessorId(int predecessorId) {
        this.predecessorId = predecessorId;
    }

    public double getLng() {
        return lng;
    }

    public void setLng(double lng) {
        this.lng = lng;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public void addEdge(Edge e) {
        edges.add(e);
    }

    public int getDistanceFromSource() {
        return distanceFromSource;
    }

    public void setDistanceFromSource(int distanceFromSource) {
        this.distanceFromSource = distanceFromSource;
    }

    public boolean isMarked() {
        return marked;
    }

    public void setMarked(boolean marked) {
        this.marked = marked;
    }

    public int getWeightForNeigbhor(Node nodeTo) {
        int weight = 0;
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).getNodeTo().equals(nodeTo)) {
                weight = edges.get(i).getWeight();
            }
        }
        return (weight);
    }

    /**
     * Get the edge that points towards the node passed as argument
     *
     * TODO what if several edges point to the same neighbor ?
     * @param neighbor the node where we want to go
     * @return Edge pointing to the Node passed
     */
    public Edge getEdgeToNeighbor(Node neighbor) {
        for (Edge edge: edges) {
            if (edge.getNodeTo().equals(neighbor)) {
                return edge;
            }
        }
        return null;
    }

    public int getLineForNeigbhor(Node nodeTo) {
        int line = 0;
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).getNodeTo().equals(nodeTo)) {
                line = edges.get(i).getLine();
            }
        }
        return line;
    }

    @Override
    public String toString() {
        return "graph.Node{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", lng=" + lng +
                ", lat=" + lat +
                '}';
    }
}
