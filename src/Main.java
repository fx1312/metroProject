import graph.Edge;
import graph.Graph;
import graph.JSONGraphFactory;
import graphproperties.BFSGraphProperties;
import graphproperties.DijkstraGraphProperties;
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

//        metro.printGraph();

        bfsDemo(metro);
//        dijkstraDemo(metro);
        edgeBetweennessDemo(metro);
    }

    private static void bfsDemo(Graph metro) {
        System.out.println();
        System.out.println();
        System.out.println();

        BFSPathFinder bfsPathFinder = new BFSPathFinder(metro);

        bfsPathFinder.computeShortestPath("Duroc", "Saint-Francois-Xavier");
        PathUtils.printPath(bfsPathFinder.getPath(), bfsPathFinder.getPathLength(), "stations");

        bfsPathFinder.computeShortestPath("Porte de Vanves", "Plaisance");
        PathUtils.printPath(bfsPathFinder.getPath(), bfsPathFinder.getPathLength(), "stations");

        bfsPathFinder.computeShortestPath("La Courneuve-8-Mai-1945", "Stalingrad");
        PathUtils.printPath(bfsPathFinder.getPath(), bfsPathFinder.getPathLength(), "stations");

        BFSGraphProperties bfsDiameter = new BFSGraphProperties(metro);

        bfsDiameter.computeRadiusAndDiameter();

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

        // GraphProperties and longest path of the metro :

        DijkstraGraphProperties dijkstraDiameter = new DijkstraGraphProperties(graph);
        dijkstraDiameter.computeRadiusAndDiameter();

        PathUtils.printPath(dijkstraDiameter.getLongestShortestPath(), dijkstraDiameter.getLongestShortestPathLength());
    }

    private static void edgeBetweennessDemo(Graph graph) {
        BFSGraphProperties bfsGraphProperties = new BFSGraphProperties(graph);
        bfsGraphProperties.computeEdgeBetweenness();

        graph.getEdges().sort((edge1, edge2) -> {
            String e1b = edge1.getNodeFrom().getName();
            String e2b = edge2.getNodeFrom().getName();
            return e2b.compareTo(e1b);
        });

        for(Edge e: graph.getEdges()) {
            System.out.println(e.getNodeFrom().getName()
                    + "/"
                    + e.getNodeTo().getName()
                    + " (ligne "
                    + e.getLine()
                    + ") "
                    + e.getBetweenness());
        }
    }
}
