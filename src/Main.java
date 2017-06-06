import graph.*;
import graphproperties.BFSGraphProperties;
import graphproperties.DijkstraGraphProperties;
import pathfinding.BFSPathFinder;
import pathfinding.DijkstraPathFinder;
import pathfinding.PathUtils;

import java.io.IOException;
import java.util.*;
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

//        simpleGraphDemo();
    }

    private static void simpleGraphDemo() {
        Graph graph = new Graph();
        List<String> lines = new ArrayList<>(Arrays.asList("1", "2", "3"));
        graph.getNodes().put(1, new Node("A", lines, 123.45, 12.45));
        graph.getNodes().put(2, new Node("B", lines, 123.45, 12.45));
        graph.getNodes().put(3, new Node("C", lines, 123.45, 12.45));
        graph.getNodes().put(4, new Node("D", lines, 123.45, 12.45));
        BFSGraphProperties bfsGraphProperties = new BFSGraphProperties(graph);
        System.out.println(bfsGraphProperties.connectedComponents());
        graph.addUndirectedEdgeBetweenNodes(1,2,"qsdf", EdgeType.METRO_LINE);
        graph.addUndirectedEdgeBetweenNodes(3,4,"qsdf", EdgeType.METRO_LINE);
        System.out.println(bfsGraphProperties.connectedComponents());
        graph.addUndirectedEdgeBetweenNodes(1,3,"qsdf", EdgeType.METRO_LINE);
        System.out.println(bfsGraphProperties.connectedComponents());
        bfsGraphProperties.computeEdgeBetweenness();
        for (Edge e: graph.getEdges()) {
            System.out.println(e.getNodeFrom().getName() + "/" + e.getNodeTo().getName() + "bet : " + e.getBetweenness());
        }

        System.out.println(graph.getEdges().size());


        // Find maximal betweenness :
        int max = -1;
        for (Edge e : graph.getEdges()) {
            if (e.getBetweenness() > max) {
                max = e.getBetweenness();
            }
        }
        // Find all edges we have to remove :
        List<Edge> edgesToRemove = new ArrayList<>();
        for (Edge e: graph.getEdges()) {
            if (e.getBetweenness() == max) {
                edgesToRemove.add(e);
            }
        }
        System.out.println("Removing " + edgesToRemove);
        graph.getEdges().removeAll(edgesToRemove);
        System.out.println(graph.getEdges().size());
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

        // GraphProperties and longest path of the metro :

        DijkstraGraphProperties dijkstraDiameter = new DijkstraGraphProperties(graph);
        dijkstraDiameter.computeRadiusAndDiameter();

        PathUtils.printPath(dijkstraDiameter.getLongestShortestPath(), dijkstraDiameter.getLongestShortestPathLength());
    }

    private static void edgeBetweennessDemo(Graph graph) {
        System.out.println();
        System.out.println();
        System.out.println();

        BFSGraphProperties bfsGraphProperties = new BFSGraphProperties(graph);
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
        }

        System.out.println("*** Girvan–Newman algorithm iterations : " + iterations);
        System.out.println("*** Girvan–Newman algorithm clusters : " + bfsGraphProperties.connectedComponents().size());
        for (List<Node> nodes : bfsGraphProperties.connectedComponents()) {
            System.out.println(String.join(", ", nodes.stream().map(n -> n.getName()).collect(Collectors.toList())));
            System.out.println(nodes.size());
        }
    }
}
