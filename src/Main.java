import graph.*;
import graphproperties.GraphProperties;
import graphproperties.PathFindingStrategy;
import pathfinding.BFSPathFinder;
import pathfinding.DijkstraPathFinder;
import pathfinding.PathUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        dijkstraDemo(metro);
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

        GraphProperties bfsGraphProperties = new GraphProperties(PathFindingStrategy.BFS, metro);

        bfsGraphProperties.computeRadiusAndDiameter();

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

        dijkstraPathFinder.computeShortestPath("Nation", "Vavin");

        PathUtils.printPath(dijkstraPathFinder.getPath(), dijkstraPathFinder.getPathLength());

        dijkstraPathFinder.computeShortestPath("Chaussee d'Antin (La Fayette)", "Madeleine");

        PathUtils.printPath(dijkstraPathFinder.getPath(), dijkstraPathFinder.getPathLength());

        dijkstraPathFinder.computeShortestPath("Cluny-La Sorbonne", "Saint-Michel");
        PathUtils.printPath(dijkstraPathFinder.getPath(), dijkstraPathFinder.getPathLength());

        // TODO what if the station names are not found ?
        dijkstraPathFinder.computeShortestPath("Gare de l'Est (Verdun)", "La Chapelle");
        PathUtils.printPath(dijkstraPathFinder.getPath(), dijkstraPathFinder.getPathLength());

        // graph properties and longest path of the metro :
        GraphProperties dijkstraDiameter = new GraphProperties(PathFindingStrategy.DIJKSTRA, graph);
        dijkstraDiameter.computeRadiusAndDiameter();

        PathUtils.printPath(dijkstraDiameter.getDiameterPath(), dijkstraDiameter.getDiameterPathLength());
    }

    private static void edgeBetweennessDemo(Graph graph) {
        System.out.println();
        System.out.println();
        System.out.println();

        GraphProperties bfsGraphProperties = new GraphProperties(PathFindingStrategy.BFS, graph);
        bfsGraphProperties.computeEdgeBetweenness();

        int targetCC = 30; // Target number of connected components in the graph

        int iterations = 0;

        while (bfsGraphProperties.connectedComponents().size() < targetCC) {
            iterations ++;

            if (graph.getEdges().size() == 0) {
                System.out.println("No edges remaining. Stopping the cluster detection algorithm");
                return;
            }

            // Find maximal betweenness :
            int max = -1;
            for (Edge e : graph.getEdges()) {
                if (e.getBetweenness() > max) {
                    max = e.getBetweenness();
                }
            }

    //        for(Edge e: graph.getEdges()) {
    //            System.out.println(e.getNodeFrom().getName()
    //                    + "/"
    //                    + e.getNodeTo().getName()
    //                    + " (ligne "
    //                    + e.getLine()
    //                    + ") "
    //                    + e.getBetweenness());
    //        }

            // Find all edges we have to remove :
            // We have to prepare this list before actual removal, or else we get a ConcurrentModificationException
            // (modifying a list while iterating over its elements)
            List<Edge> edgesToRemove = new ArrayList<>();
            for (Edge e: graph.getEdges()) {
                if (e.getBetweenness() == max) {
                    edgesToRemove.add(e);
                }
            }


            for (Edge e: edgesToRemove) {
                System.out.println("Removing line " + e.getLine() + " from " + e.getNodeFrom().getName() + " to " + e.getNodeTo().getName() + "(edge betweenness : " + e.getBetweenness() + ")");
                graph.removeEdge(e);
            }

            // Recompute edge betweenness, taking into account the removal of some Edges :
            bfsGraphProperties.computeEdgeBetweenness();
        }

        System.out.println("*** Girvan–Newman algorithm iterations : " + iterations);
        System.out.println("*** Girvan–Newman algorithm clusters : " + bfsGraphProperties.connectedComponents().size());
        for (List<Node> nodes : bfsGraphProperties.connectedComponents()) {
            System.out.println(String.join(", ", nodes.stream().map(n -> n.getName()).collect(Collectors.toList())));
            System.out.println(nodes.size());
        }
    }
}
