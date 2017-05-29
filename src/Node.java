import com.peertopark.java.geocalc.*;

import java.util.ArrayList;
import java.util.List;

public class Node {
    private List<Edge> edges = new ArrayList<>();
    private String name;
    private int id;
    private List<Integer> lines = new ArrayList<>();
    private List<Node> neighbors = new ArrayList<>();
    private int predecessor = -1;
    private double lng;
    private double lat;
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

    public int getId() {
        return id;
    }

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

    public int getPredecessor() {
        return predecessor;
    }

    public void setPredecessor(int predecessor) {
        this.predecessor = predecessor;
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

    public void fillNeighbors() {
        for (int i = 0; i < edges.size(); i++) {
            neighbors.add(edges.get(i).getNodeTo());
        }
    }

    //Utilise une API pour calculer la distance en metre à partir de deux points dont
    //on connait les coordonnées. Ajoute ensuite cette distance dans le poids du lien.
    public void fillWeights() {
        for (int i = 0; i < edges.size(); i++) {

            Coordinate latStation1 = new DegreeCoordinate(edges.get(i).getNodeFrom().getLat());
            Coordinate lngStation1 = new DegreeCoordinate(edges.get(i).getNodeFrom().getLng());
            Point station1 = new Point(latStation1, lngStation1);

            Coordinate latStation2 = new DegreeCoordinate(edges.get(i).getNodeTo().getLat());
            Coordinate lngStation2 = new DegreeCoordinate(edges.get(i).getNodeTo().getLng());
            Point station2 = new Point(latStation2, lngStation2);

            edges.get(i).setWeight((int) EarthCalc.getHarvesineDistance(station1, station2));
        }


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

    public int getLineForNeigbhor(Node nodeTo) {
        int line = 0;
        for (int i = 0; i < edges.size(); i++) {
            if (edges.get(i).getNodeTo().equals(nodeTo)) {
                line = edges.get(i).getLine();
            }
        }
        return (line);
    }

    @Override
    public String toString() {
        return "Node{" +
                "name='" + name + '\'' +
                ", id=" + id +
                ", lng=" + lng +
                ", lat=" + lat +
                '}';
    }
}
