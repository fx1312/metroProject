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

        addEdgesBetweenStations(graph, routes);

        addFootConnectionsToStations(graph, correspondances);

        computeNeighborsAndWeights(graph);

        return graph;
    }

    private static void addFootConnectionsToStations(Graph graph, JSONArray correspondances) {
        // Iterate over the list of
        for (int i = 0; i < correspondances.length(); i++) {
            JSONArray correspondance = correspondances.getJSONArray(i);

            Map<Integer, Node> nodes = graph.getNodes();

            List<Integer> nodesToLink = new ArrayList<>();

            // Only keep stations we know about (aka metro stations) :
            for (int j = 0; j < correspondance.length(); j ++) {
                try {
                    String correspondanceLine = correspondance.getString(j);
                    int nodeIndex = Integer.parseInt(correspondanceLine);
                    Node node = nodes.get(nodeIndex);
                    if (node != null) nodesToLink.add(nodeIndex);
                } catch (NumberFormatException e) {
                    // No-op ! This station was not recognised, we go on to the next one
                }
            }

            if (nodesToLink.size() == 2) {
                addUndirectedEdgeBetweenNodes(graph, nodesToLink.get(0), nodesToLink.get(1), "Correspondance à pied", EdgeType.CONNECTION);
            }

            if (nodesToLink.size() == 3) {
                addUndirectedEdgeBetweenNodes(graph, nodesToLink.get(0), nodesToLink.get(1), "Correspondance à pied", EdgeType.CONNECTION);
                addUndirectedEdgeBetweenNodes(graph, nodesToLink.get(0), nodesToLink.get(2), "Correspondance à pied", EdgeType.CONNECTION);
                addUndirectedEdgeBetweenNodes(graph, nodesToLink.get(1), nodesToLink.get(2), "Correspondance à pied", EdgeType.CONNECTION);
            }

            if (nodesToLink.size() > 3) {
                throw new RuntimeException("Tu m'as oublié, Jack");
            }
        }
    }

    private static void addEdgesBetweenStations(Graph graph, JSONArray routes) {
        for (int i = 0; i < routes.length(); i++) {
            // Cré une liste contenant la correspondance (deux ou trois arrêts)
            JSONObject route = routes.getJSONObject(i);

            String routeType = route.getString("type");

            // We only need to handle metro routes (we don't care about other means of transportations,
            // and connections are handled using the fact that a station has several edges from/to itself)
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
        }
    }

    private static void addStationsToGraph(Graph graph, JSONObject stations) {
        Map<Integer, Node> nodes = graph.getNodes();
        Iterator iteratorStation = stations.keys();

        // Parcourt les stations et les ajoute au graph.
        while (iteratorStation.hasNext()) {
            // Cré un objet JSON avec les données de la station
            JSONObject station = stations.getJSONObject(iteratorStation.next().toString());

            List<String> linesStationBelongsTo = new ArrayList<>();

            // Ne sélectionne que les stations de type "metro"
            if (station.get("type").toString().equals("metro")) {

                // Remplis la variable temporaire des lignes auxquels appartiennet les stations
                JSONObject lignesMetro = station.getJSONObject("lignes");
                JSONArray listeLignesMetro = lignesMetro.getJSONArray("metro");

                for (int i = 0; i < listeLignesMetro.length(); i++) {
                    linesStationBelongsTo.add(listeLignesMetro.getString(i));
                }

                // Creates the station and add it to the graph :
                Integer id = Integer.parseInt(station.get("num").toString());
                nodes.put(id,
                        new Node(
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
            node.addNeighbor(edge.getNodeTo());

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

    private static void addUndirectedEdgeBetweenNodes(Graph graph, int node1Index, int node2Index, String line, EdgeType type) {
        addEdgeBetweenNodes(graph, node1Index, node2Index, line, type);
        addEdgeBetweenNodes(graph, node2Index, node1Index, line, type);
    }

    private static void addEdgeBetweenNodes(Graph graph, int node1Index, int node2Index, String line, EdgeType type) {
        Map<Integer, Node> nodes = graph.getNodes();

        Node node1 = nodes.get(node1Index);
        Node node2 = nodes.get(node2Index);

        Edge edge = new Edge(node1, node2, line, type);

        node1.addEdge(edge);
    }
}
