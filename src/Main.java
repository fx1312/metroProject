import com.peertopark.java.geocalc.Coordinate;
import com.peertopark.java.geocalc.DegreeCoordinate;
import com.peertopark.java.geocalc.EarthCalc;
import com.peertopark.java.geocalc.Point;
import graph.Edge;
import graph.Graph;
import graph.JSONGraphFactory;
import pathfinding.DijkstraPathfinder;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Graph metro = null;
        try {
            metro = JSONGraphFactory.createFromJSONFile("reseaux.json");
        } catch (IOException e) {
            System.out.println("Graph creation failed. Nested exception is logged below.");
            e.printStackTrace();
        }

        DijkstraPathfinder dijkstraPathfinder = new DijkstraPathfinder(metro);

        metro.printGraph();
        System.out.println();
        System.out.println();

        dijkstraPathfinder.computeShortestPath("Nation", "Bastille");

        System.out.format("Longueur du trajet : %s", dijkstraPathfinder.getPathLength());
        System.out.println(); // TODO system.ls

        for (Edge edge: dijkstraPathfinder.getPath()) {
            System.out.format("* Ligne %s entre %s et %s", edge.getLine(), edge.getNodeFrom().getName(), edge.getNodeTo().getName());
            System.out.println();
        }
    }

    public static void testGeocalcDistance() {
        // Javel, Paris
        Coordinate lat = new DegreeCoordinate(48.8461034637876);
        Coordinate lng = new DegreeCoordinate(2.27845497841475);
        Point javel = new Point(lat, lng);

        // Charles-Michel, Paris
        Coordinate lat1 = new DegreeCoordinate(48.846336);
        Coordinate lng1 = new DegreeCoordinate(2.2863324);
        Point charles = new Point(lat1, lng1);

        int distance = (int) EarthCalc.getHarvesineDistance(javel, charles); // in meters

        System.out.println("Distance: " + distance);
    }
}
