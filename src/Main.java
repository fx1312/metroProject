import com.peertopark.java.geocalc.Coordinate;
import com.peertopark.java.geocalc.DegreeCoordinate;
import com.peertopark.java.geocalc.EarthCalc;
import com.peertopark.java.geocalc.Point;
import com.sun.javafx.runtime.SystemProperties;
import graph.Graph;
import graph.JSONGraphFactory;
import graphproperties.BFSDiameter;
import graphproperties.DijkstraDiameter;
import pathfinding.BFSPathFinder;
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

        metro.printGraph();

        bfsDemo(metro);
        System.out.println("");

        dijkstraDemo(metro);


    }

    private static void bfsDemo(Graph metro) {
        BFSPathFinder bfsPathFinder = new BFSPathFinder(metro);
        bfsPathFinder.computeShortestPath("La Courneuve-8-Mai-1945", "Stalingrad");

        PathUtils.printPath(bfsPathFinder.getPath(), bfsPathFinder.getPathLength());

        BFSDiameter bfsDiameter = new BFSDiameter(metro);

        bfsDiameter.computeGraphProperties();

        PathUtils.printPath(
                bfsDiameter.getLongestShortestPath(),
                bfsDiameter.getLongestShortestPathLength(),
                "stations"
        );
    }

    private static void dijkstraDemo(Graph graph) {
        DijkstraPathfinder dijkstraPathfinder = new DijkstraPathfinder(graph);

        System.out.println();
        System.out.println();

        dijkstraPathfinder.computeShortestPath("Nation", "Vavin");

        PathUtils.printPath(dijkstraPathfinder.getPath(), dijkstraPathfinder.getPathLength());

        dijkstraPathfinder.computeShortestPath("Chaussee d'Antin (La Fayette)", "Madeleine");

        PathUtils.printPath(dijkstraPathfinder.getPath(), dijkstraPathfinder.getPathLength());

        dijkstraPathfinder.computeShortestPath("Cluny-La Sorbonne", "Saint-Michel");
        PathUtils.printPath(dijkstraPathfinder.getPath(), dijkstraPathfinder.getPathLength());

        // TODO what if the station names are not found ?
        dijkstraPathfinder.computeShortestPath("Gare de l'Est (Verdun)", "La Chapelle");
        PathUtils.printPath(dijkstraPathfinder.getPath(), dijkstraPathfinder.getPathLength());

        // Diameter and longest path of the metro :

        DijkstraDiameter dijkstraDiameter = new DijkstraDiameter(graph);
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
