package graph;

import com.peertopark.java.geocalc.Coordinate;
import com.peertopark.java.geocalc.DegreeCoordinate;
import com.peertopark.java.geocalc.EarthCalc;
import com.peertopark.java.geocalc.Point;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class JSONGraphFactory {
    public static Graph createFromJSONFile(String filepath) throws IOException {
        // Ouvre le JSON.
        String text = new String(Files.readAllBytes(Paths.get(filepath)), StandardCharsets.UTF_8);

        // Cré un objet JSON dont on peut récupérer les données
        JSONObject json = new JSONObject(text);

        return createFromJSONObject(json);
    }

    private static Graph createFromJSONObject(JSONObject json) {
        // Cré un objet JSON qui contient la liste des stations + un itérateur pour les parcourir
        JSONObject stations = json.getJSONObject("stations");

        // Cré un objet JSON qui contient les données sur les lignes + un itérateur pour les parcourir
        JSONObject lignes = json.getJSONObject("lignes");

        // Cré un tableau JSON qui contient les données de correspondances
        JSONArray correspondances = json.getJSONArray("corresp");

        JSONArray routes = json.getJSONArray("routes");

        Graph graph = new Graph();

        addStationsToGraph(graph, stations);

//        addLinesToStations(graph, lignes);

        addEdgesBetweenStations(graph, routes);

        addConnectionsToStations(graph, correspondances);

        computeNeighborsAndWeights(graph);

        return graph;
    }

    private static void addConnectionsToStations(Graph graph, JSONArray correspondances) {
        for (int i = 0; i < correspondances.length(); i++) {

            // Cré une liste contenant la correspondance (deux ou trois arrêts)
            JSONArray correspTemp = correspondances.getJSONArray(i);

            Map<Integer, Node> nodes = graph.getNodes();

            // Si la correspondance est constitué de deux arrets de type metro
            if (correspTemp.length() == 2 && !correspTemp.getString(0).contains("_") && !correspTemp.getString(1).contains("_") && nodes.containsKey(Integer.parseInt(correspTemp.getString(0))) && nodes.containsKey(Integer.parseInt(correspTemp.getString(1)))) {

                int node1Index = Integer.parseInt(correspTemp.getString(0));
                int node2Index = Integer.parseInt(correspTemp.getString(1));

                // TODO addUndirectedEdge and addDirectedEdge :
                addEdgeBetweenNodes(graph, node1Index, node2Index, "connection", EdgeType.CONNECTION);
                addEdgeBetweenNodes(graph, node2Index, node1Index, "connection", EdgeType.CONNECTION);
            }

            // Si la correspondance est constitué de trois arrets de type metro
            if (correspTemp.length() == 3 && !correspTemp.getString(0).contains("_") && !correspTemp.getString(1).contains("_") && !correspTemp.getString(2).contains("_") && nodes.containsKey(Integer.parseInt(correspTemp.getString(0))) && nodes.containsKey(Integer.parseInt(correspTemp.getString(1))) && nodes.containsKey(Integer.parseInt(correspTemp.getString(2)))) {


                System.out.println("swaggity swag"); // never printed out ==> condition above seems to be always false


                nodes.get(Integer.parseInt(correspTemp.get(0).toString()))
                        .addEdge(
                                new Edge(
                                        nodes.get(Integer.parseInt(correspTemp.getString(0))),
                                        nodes.get(Integer.parseInt(correspTemp.getString(1))),
                                        "connection",
                                        EdgeType.CONNECTION
                                ));

                nodes.get(Integer.parseInt(correspTemp.get(0).toString()))
                        .addEdge(
                                new Edge(
                                        nodes.get(Integer.parseInt(correspTemp.getString(1))),
                                        nodes.get(Integer.parseInt(correspTemp.getString(0))),
                                        "connection",
                                        EdgeType.CONNECTION
                                ));

                nodes.get(Integer.parseInt(correspTemp.get(0).toString()))
                        .addEdge(
                                new Edge(
                                        nodes.get(Integer.parseInt(correspTemp.getString(0))),
                                        nodes.get(Integer.parseInt(correspTemp.getString(2))),
                                        "connection",
                                        EdgeType.CONNECTION
                                ));

                nodes.get(Integer.parseInt(correspTemp.get(0).toString()))
                        .addEdge(
                                new Edge(
                                        nodes.get(Integer.parseInt(correspTemp.getString(2))),
                                        nodes.get(Integer.parseInt(correspTemp.getString(0))),
                                        "connection",
                                        EdgeType.CONNECTION
                                ));

                nodes.get(Integer.parseInt(correspTemp.get(0).toString()))
                        .addEdge(
                                new Edge(
                                        nodes.get(Integer.parseInt(correspTemp.getString(2))),
                                        nodes.get(Integer.parseInt(correspTemp.getString(3))),
                                        "connection",
                                        EdgeType.CONNECTION
                                ));

                nodes.get(Integer.parseInt(correspTemp.get(0).toString()))
                        .addEdge(
                                new Edge(
                                        nodes.get(Integer.parseInt(correspTemp.getString(3))),
                                        nodes.get(Integer.parseInt(correspTemp.getString(2))),
                                        "connection",
                                        EdgeType.CONNECTION
                                ));
            }
        }
    }

    private static void addEdgesBetweenStations(Graph graph, JSONArray routes) {
        for (int i = 0; i < routes.length(); i++) {
            // Cré une liste contenant la correspondance (deux ou trois arrêts)
            JSONObject route = routes.getJSONObject(i);

            String routeType = route.getString("type");

            if (routeType.equals("metro")) {
                JSONArray stops = route.getJSONArray("arrets");
                String lineName = route.getString("ligne");

                // Iterate over the stops [0, n - 1] to add edges between them
                // We add a single, directed edge because "routes" in the JSON contains routes for each
                // destination of the metro lines :
                for (int j = 0; j < stops.length() - 1; j++) {
                    int nodeFromIndex = Integer.parseInt(stops.getString(j));
                    int nodeToIndex = Integer.parseInt(stops.getString(j + 1));

                    addEdgeBetweenNodes(graph, nodeFromIndex, nodeToIndex, lineName, EdgeType.METRO_LINE);
                }
            }

//            if (routeType.equals("corresp")) {
//                JSONArray stops = route.getJSONArray("arrets");
//                String lineName = "connection";
//                for (int j = 0; j < stops.length(); j++) {
//                    int nodeFromIndex = Integer.parseInt(stops.getString(j));
//                    int nodeToIndex = Integer.parseInt(stops.getString(j + 1));
//
//                    addEdgeBetweenNodes(graph, nodeFromIndex, nodeToIndex, lineName, EdgeType.METRO_LINE);
//                }
//            }
        }
    }

    private static void addLinesToStations(Graph graph, JSONObject lignes) {
        Iterator iteratorLignes = lignes.keys();

        while (iteratorLignes.hasNext()) {

            String lineName = iteratorLignes.next().toString();

            // Cré un objet JSON contenant les données de la ligne.
            JSONObject ligne = lignes.getJSONObject(lineName);

            // Ne cré les connexions que pour les lignes de metro.
            if (ligne.has("arrets") && ligne.get("type").equals("metro")) {

                // Cré  un tableau contenant les arrets de la ligne.
                JSONArray arretsTab = ligne.getJSONArray("arrets");

                for (int j = 0; j < arretsTab.length(); j++) {

                    // Cré une liste contenant les arrets de la ligne dans l'ordre réel
                    JSONArray arrets = arretsTab.getJSONArray(j);

                    // Pour toute le lignes sauf la 10 (cas spécial)
                    if (!lineName.equals("10")) {
                        for (int i = 0; i < arrets.length() - 1; i++) {

                            int node1index = Integer.parseInt(arrets.getString(i));
                            int node2index = Integer.parseInt(arrets.getString(i + 1));

                            addEdgeBetweenNodes(graph, node1index, node2index, lineName, EdgeType.METRO_LINE);
                            addEdgeBetweenNodes(graph, node2index, node1index, lineName, EdgeType.METRO_LINE);
                        }
                    } else {
                        // Cas de la ligne 10
                        // TODO this is completely broken (try pathfinder.dijkstra("Boulogne-Jean-Jaures", "Javel-Andre-Citroen");)
                        if (j == 0) {
                            for (int i = 0; i < arrets.length() - 1; i++) {

                                int node1index = Integer.parseInt(arrets.getString(i));
                                int node2index = Integer.parseInt(arrets.getString(i + 1));

                                addEdgeBetweenNodes(graph, node1index, node2index, lineName, EdgeType.METRO_LINE);
                            }

                            for (int i = arrets.length() - 1; i > 5; i--) {

                                int node1index = Integer.parseInt(arrets.getString(i));
                                int node2index = Integer.parseInt(arrets.getString(i - 1));

                                addEdgeBetweenNodes(graph, node1index, node2index, lineName, EdgeType.METRO_LINE);
                            }
                        }
                        if (j == 1) {
                            for (int i = arrets.length() - 1; i > 0; i--) {

                                int node1index = Integer.parseInt(arrets.getString(i));
                                int node2index = Integer.parseInt(arrets.getString(i - 1));

                                addEdgeBetweenNodes(graph, node1index, node2index, lineName, EdgeType.METRO_LINE);
                            }
                        }
                    }
                }
            }
        }
    }

    private static void addStationsToGraph(Graph graph, JSONObject stations) {
        Map<Integer, Node> nodes = graph.getNodes();
        Iterator iteratorStation = stations.keys();

        // Parcour les stations et les ajoute au graph.
        while (iteratorStation.hasNext()) {

            // Variable temporaire qui stocke les lignes auxquels appartiennent les stations
            List<Integer> linesStationBelongsTo = new ArrayList<>();

            // Cré un objet JSON avec les données de la station
            JSONObject station = stations.getJSONObject(iteratorStation.next().toString());

            // Ne sélectionne que les stations de type "metro"
            if (station.get("type").toString().equals("metro")) {

                // Remplis la variable temporaire des lignes auxquels appartiennet les stations
                JSONObject lignesMetro = station.getJSONObject("lignes");
                JSONArray listeLignesMetro = lignesMetro.getJSONArray("metro");

                for (int i = 0; i < listeLignesMetro.length(); i++) {
                    if (listeLignesMetro.get(i).equals("3B") || listeLignesMetro.get(i).equals("7B")) {
                        if (listeLignesMetro.get(i).equals("3B")) {
                            linesStationBelongsTo.add(3);
                        } else {
                            linesStationBelongsTo.add(7);
                        }
                    } else {
                        linesStationBelongsTo.add(Integer.parseInt(listeLignesMetro.get(i).toString()));
                    }
                }

                // Cré la station ainsi que ses différentes propriétés et l'ajoute au graphe.
                nodes.put(Integer.parseInt(station.get("num").toString()),
                        new Node(
                                Integer.parseInt(station.get("num").toString()),
                                station.get("nom").toString(),
                                linesStationBelongsTo,
                                Double.parseDouble(station.get("lng").toString()),
                                Double.parseDouble(station.get("lat").toString())
                        )
                );
            }
        }
    }

    private static void computeNeighborsAndWeights(Graph graph) {
        Map<Integer, Node> nodes = graph.getNodes();

        nodes.forEach((Integer key, Node node) -> JSONGraphFactory.computeNodeNeighborsAndWeights(node));
    }

    private static void computeNodeNeighborsAndWeights(Node node) {
        List<Edge> edges = node.getEdges();

        edges.forEach(edge ->  {
            // Computing Node's neighbors :
            node.getNeighbors().add(edge.getNodeTo());

            // Computing edges weights using geocalc to calculate the distance between two (lat, lng) pairs :
            Coordinate latStation1 = new DegreeCoordinate(edge.getNodeFrom().getLat());
            Coordinate lngStation1 = new DegreeCoordinate(edge.getNodeFrom().getLng());
            Point station1 = new Point(latStation1, lngStation1);

            Coordinate latStation2 = new DegreeCoordinate(edge.getNodeTo().getLat());
            Coordinate lngStation2 = new DegreeCoordinate(edge.getNodeTo().getLng());
            Point station2 = new Point(latStation2, lngStation2);

            // distance in meters :
            Double distance = EarthCalc.getHarvesineDistance(station1, station2);
            Long dL = Math.round(distance);
            edge.setWeight(dL.intValue());
        });
    }

    private static void addEdgeBetweenNodes(Graph graph, int node1Index, int node2Index, String line, EdgeType type) {
        // TODO use graph.findNodeById, deprecate graph.getNodes ?

        Map<Integer, Node> nodes = graph.getNodes();

        Node node1 = nodes.get(node1Index);
        Node node2 = nodes.get(node2Index);
        Edge edge = new Edge(node1, node2, line, type);

        node1.addEdge(edge);
    }
}
