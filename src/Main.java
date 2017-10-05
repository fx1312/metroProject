import graph.Graph;
import graph.JSONGraphFactory;
import graphproperties.GraphProperties;
import pathfinding.BFSPathFinder;
import pathfinding.DijkstraPathFinder;
import pathfinding.PathUtils;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Graph metro = buildGraph();

        if (metro == null) return;

        metro.printGraph();

        bfsDemo(metro);
        dijkstraDemo(metro);
        girvanNewman(metro);
    }

    private static Graph buildGraph() {
        try {
            return JSONGraphFactory.createFromJSONFile("reseaux.json");
        } catch (IOException e) {
            System.out.println("Graph creation failed. Nested exception is logged below.");
            e.printStackTrace();
        }
        return null;
    }

    private static void bfsDemo(Graph metro) {
        System.out.println();
        System.out.println();
        System.out.println();

        System.out.println("*** RECHERCHE DE PLUS COURTS CHEMINS AVEC L'ALGORITHME BFS ***");

        BFSPathFinder bfsPathFinder = new BFSPathFinder(metro);

        bfsPathFinder.computeShortestPath("La Courneuve-8-Mai-1945", "Stalingrad");
        PathUtils.printPath(bfsPathFinder.getPath(), bfsPathFinder.getPathLength(), "interstations");

        bfsPathFinder.computeShortestPath("Notre-Dame des Champs", "Corentin-Celton");
        PathUtils.printPath(bfsPathFinder.getPath(), bfsPathFinder.getPathLength(), "interstations");

        bfsPathFinder.computeShortestPath("Nation", "Vavin");
        PathUtils.printPath(bfsPathFinder.getPath(), bfsPathFinder.getPathLength(), "interstations");

        GraphProperties bfsGraphProperties = new GraphProperties(new BFSPathFinder(metro), metro);
        bfsGraphProperties.computeRadiusAndDiameter();

        System.out.println();
        System.out.format("Graph radius : %s", bfsGraphProperties.getRadius());
        System.out.println();
        System.out.println("Path associated with the graph diameter :");
        PathUtils.printPath(
                bfsGraphProperties.getDiameterPath(),
                bfsGraphProperties.getDiameterPathLength(),
                "stations"
        );
    }

    private static void dijkstraDemo(Graph graph) {
        DijkstraPathFinder dijkstraPathFinder = new DijkstraPathFinder(graph);

        System.out.println();
        System.out.println();
        System.out.println();

        System.out.println("*** RECHERCHE DE PLUS COURTS CHEMINS AVEC L'ALGORITHME DE DIJKSTRA ***");

        dijkstraPathFinder.computeShortestPath("La Courneuve-8-Mai-1945", "Stalingrad");
        PathUtils.printPath(dijkstraPathFinder.getPath(), dijkstraPathFinder.getPathLength());

        dijkstraPathFinder.computeShortestPath("Notre-Dame des Champs", "Corentin-Celton");
        PathUtils.printPath(dijkstraPathFinder.getPath(), dijkstraPathFinder.getPathLength());

        dijkstraPathFinder.computeShortestPath("Nation", "Vavin");
        PathUtils.printPath(dijkstraPathFinder.getPath(), dijkstraPathFinder.getPathLength());

        // graph properties and longest path of the metro :
        GraphProperties dijkstraGraphProperties = new GraphProperties(new DijkstraPathFinder(graph), graph);
        dijkstraGraphProperties.computeRadiusAndDiameter();

        System.out.format("Graph radius : %s", dijkstraGraphProperties.getRadius());
        System.out.println();
        System.out.println("Path associated with the graph diameter :");
        PathUtils.printPath(dijkstraGraphProperties.getDiameterPath(), dijkstraGraphProperties.getDiameterPathLength());
    }

    private static void girvanNewman(Graph graph) {
        System.out.println();
        System.out.println();
        System.out.println("*** GIRVAN-NEWMAN ALGORITHM WITH BFS ***");

        GraphProperties bfsGraphProperties = new GraphProperties(new BFSPathFinder(graph), graph);
        bfsGraphProperties.girvanNewman();

        // G/N algorithm alters the graph so we have to rebuild it (could be more optimal)
        graph = buildGraph();

        System.out.println();
        System.out.println();
        System.out.println("*** GIRVAN-NEWMAN ALGORITHM WITH DIJKSTRA ***");

        GraphProperties dijkstraGraphProperties = new GraphProperties(new DijkstraPathFinder(graph), graph);
        dijkstraGraphProperties.girvanNewman();
    }
}
