import com.peertopark.java.geocalc.Coordinate;
import com.peertopark.java.geocalc.DegreeCoordinate;
import com.peertopark.java.geocalc.EarthCalc;
import com.peertopark.java.geocalc.Point;
import graph.Graph;
import graph.JSONGraphFactory;
import graphproperties.DijkstraDiameter;
import pathfinding.DijkstraPathfinder;
import pathfinding.PathUtils;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Graph metro;
        try {
            metro = JSONGraphFactory.createFromJSONFile("reseaux.json");
        } catch (IOException e) {
            System.out.println("Graph creation failed. Nested exception is logged below.");
            e.printStackTrace();
            return;
        }


        // Pathfinder using Dijkstra's algorithm :

        DijkstraPathfinder dijkstraPathfinder = new DijkstraPathfinder(metro);

        metro.printGraph();
        System.out.println();
        System.out.println();

        dijkstraPathfinder.computeShortestPath("Nation", "Vavin");

        PathUtils.printPath(dijkstraPathfinder.getPath(), dijkstraPathfinder.getPathLength());

        dijkstraPathfinder.computeShortestPath("Etienne Marcel", "Hotel de Ville");

        PathUtils.printPath(dijkstraPathfinder.getPath(), dijkstraPathfinder.getPathLength());

        // Diameter and longest path of the metro :

        DijkstraDiameter dijkstraDiameter = new DijkstraDiameter(metro);
        dijkstraDiameter.computeGraphProperties();

        PathUtils.printPath(dijkstraDiameter.getLongestShortestPath(), dijkstraDiameter.getLongestShortestPathLength());
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
