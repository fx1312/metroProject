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
import java.util.*;

public class Graph {
    private Map<Integer, Node> allNodes = new HashMap<>();

    public Map<Integer, Node> getNodes() {
        return allNodes;
    }

    Graph(String path) {
        try {
            // Ouvre le JSON.
            String text = new String(Files.readAllBytes(Paths.get(path)), StandardCharsets.UTF_8);

            // Cré un objet JSON dont on peut récupérer les données
            JSONObject json = new JSONObject(text);

            // Cré un objet JSON qui contient la liste des stations + un itérateur pour les parcourir
            JSONObject stations = json.getJSONObject("stations");

            // Cré un objet JSON qui contient les données sur les lignes + un itérateur pour les parcourir
            JSONObject lignes = json.getJSONObject("lignes");

            // Cré un tableau JSON qui contient les données de correspondances
            JSONArray correspondances = json.getJSONArray("corresp");

            addStationsToMap(stations);

            addLinesToStations(lignes);

            addConnectionsToStations(correspondances);

            // What does this do ?
            for (Map.Entry<Integer, Node> mapentry : allNodes.entrySet()) {
                mapentry.getValue().fillNeighbors();
                mapentry.getValue().fillWeights();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void addConnectionsToStations(JSONArray correspondances) {
        for (int i = 0; i < correspondances.length(); i++) {

            // Cré une liste contenant la correspondance (deux ou trois arrêts)
            JSONArray correspTemp = correspondances.getJSONArray(i);

            // Si la correspondance est constitué de deux arrets de type metro
            if (correspTemp.length() == 2 && !correspTemp.getString(0).contains("_") && !correspTemp.getString(1).contains("_") && allNodes.containsKey(Integer.parseInt(correspTemp.getString(0))) && allNodes.containsKey(Integer.parseInt(correspTemp.getString(1)))) {

                allNodes.get(Integer.parseInt(correspTemp.get(0).toString()))
                        .addEdge(
                                new Edge(
                                        allNodes.get(Integer.parseInt(correspTemp.getString(0))),
                                        allNodes.get(Integer.parseInt(correspTemp.getString(1))),
                                        -1,
                                        "correspondance"
                                ));

                allNodes.get(Integer.parseInt(correspTemp.get(0).toString()))
                        .addEdge(
                                new Edge(
                                        allNodes.get(Integer.parseInt(correspTemp.getString(1))),
                                        allNodes.get(Integer.parseInt(correspTemp.getString(0))),
                                        -1,
                                        "correspondance"
                                ));
            }

            // Si la correspondance est constitué de trois arrets de type metro
            if (correspTemp.length() == 3 && !correspTemp.getString(0).contains("_") && !correspTemp.getString(1).contains("_") && !correspTemp.getString(2).contains("_") && allNodes.containsKey(Integer.parseInt(correspTemp.getString(0))) && allNodes.containsKey(Integer.parseInt(correspTemp.getString(1))) && allNodes.containsKey(Integer.parseInt(correspTemp.getString(2)))) {
                allNodes.get(Integer.parseInt(correspTemp.get(0).toString()))
                        .addEdge(
                                new Edge(
                                        allNodes.get(Integer.parseInt(correspTemp.getString(0))),
                                        allNodes.get(Integer.parseInt(correspTemp.getString(1))),
                                        -1,
                                        "correspondance"
                                ));

                allNodes.get(Integer.parseInt(correspTemp.get(0).toString()))
                        .addEdge(
                                new Edge(
                                        allNodes.get(Integer.parseInt(correspTemp.getString(1))),
                                        allNodes.get(Integer.parseInt(correspTemp.getString(0))),
                                        -1,
                                        "correspondance"
                                ));

                allNodes.get(Integer.parseInt(correspTemp.get(0).toString()))
                        .addEdge(
                                new Edge(
                                        allNodes.get(Integer.parseInt(correspTemp.getString(0))),
                                        allNodes.get(Integer.parseInt(correspTemp.getString(2))),
                                        -1,
                                        "correspondance"
                                ));

                allNodes.get(Integer.parseInt(correspTemp.get(0).toString()))
                        .addEdge(
                                new Edge(
                                        allNodes.get(Integer.parseInt(correspTemp.getString(2))),
                                        allNodes.get(Integer.parseInt(correspTemp.getString(0))),
                                        -1,
                                        "correspondance"
                                ));

                allNodes.get(Integer.parseInt(correspTemp.get(0).toString()))
                        .addEdge(
                                new Edge(
                                        allNodes.get(Integer.parseInt(correspTemp.getString(2))),
                                        allNodes.get(Integer.parseInt(correspTemp.getString(3))),
                                        -1,
                                        "correspondance"
                                ));

                allNodes.get(Integer.parseInt(correspTemp.get(0).toString()))
                        .addEdge(
                                new Edge(
                                        allNodes.get(Integer.parseInt(correspTemp.getString(3))),
                                        allNodes.get(Integer.parseInt(correspTemp.getString(2))),
                                        -1,
                                        "correspondance"
                                ));
            }
        }
    }

    private void addLinesToStations(JSONObject lignes) {
        Iterator iteratorLignes = lignes.keys();

        while (iteratorLignes.hasNext()) {

            String numeroLigneString = iteratorLignes.next().toString();

            // Cré un objet JSON contenant les données de la ligne.
            JSONObject ligne = lignes.getJSONObject(numeroLigneString);

            // Ne cré les connexions que pour les lignes de metro.
            if (ligne.has("arrets") && ligne.get("type").equals("metro")) {

                int numeroLigneInt;

                if (numeroLigneString.equals("3B") || numeroLigneString.equals("7B")) {
                    if (numeroLigneString.equals("3B")) {
                        numeroLigneInt = 3;
                    } else {
                        numeroLigneInt = 7;
                    }
                } else {
                    numeroLigneInt = Integer.parseInt(numeroLigneString);
                }

                // Cré  un tableau contenant les arrets de la ligne.
                JSONArray arretsTab = ligne.getJSONArray("arrets");

                for (int j = 0; j < arretsTab.length(); j++) {

                    // Cré une liste contenant les arrets de la ligne dans l'ordre réel
                    JSONArray arrets = arretsTab.getJSONArray(j);

                    // Pour toute le lignes sauf la 10 (cas spécial)
                    if (numeroLigneInt != 10) {
                        for (int i = 0; i < arrets.length() - 1; i++) {

                            // Ajoute une lien entre chaque station dans les deux sens.
                            allNodes
                                    .get(Integer.parseInt(arrets.getString(i)))
                                    .addEdge(
                                            new Edge(
                                                    allNodes.get(Integer.parseInt(arrets.getString(i))),
                                                    allNodes.get(Integer.parseInt(arrets.getString(i + 1))),
                                                    numeroLigneInt,
                                                    "metro"
                                            )
                                    );

                            allNodes
                                    .get(Integer.parseInt(arrets.getString(i + 1)))
                                    .addEdge(
                                            new Edge(
                                                    allNodes.get(Integer.parseInt(arrets.getString(i + 1))),
                                                    allNodes.get(Integer.parseInt(arrets.getString(i))),
                                                    numeroLigneInt,
                                                    "metro"
                                            )
                                    );
                        }
                    } else {
                        // Cas de la ligne 10
                        if (j == 0) {
                            for (int i = 0; i < arrets.length() - 1; i++) {

                                // Ajoute une lien entre chaque station dans les deux sens.
                                allNodes
                                        .get(Integer.parseInt(arrets.getString(i)))
                                        .addEdge(
                                                new Edge(
                                                        allNodes.get(Integer.parseInt(arrets.getString(i))),
                                                        allNodes.get(Integer.parseInt(arrets.getString(i + 1))),
                                                        numeroLigneInt,
                                                        "metro"
                                                )
                                        );
                            }

                            for (int i = arrets.length() - 1; i > 5; i--) {

                                // Ajoute une lien entre chaque station dans les deux sens.
                                allNodes
                                        .get(Integer.parseInt(arrets.getString(i)))
                                        .addEdge(
                                                new Edge(
                                                        allNodes.get(Integer.parseInt(arrets.getString(i))),
                                                        allNodes.get(Integer.parseInt(arrets.getString(i - 1))),
                                                        numeroLigneInt,
                                                        "metro"
                                                )
                                        );
                            }
                        }
                        if (j == 1) {
                            for (int i = arrets.length() - 1; i > 0; i--) {

                                // Ajoute une lien entre chaque station dans les deux sens.
                                allNodes
                                        .get(Integer.parseInt(arrets.getString(i)))
                                        .addEdge(
                                                new Edge(
                                                        allNodes.get(Integer.parseInt(arrets.getString(i))),
                                                        allNodes.get(Integer.parseInt(arrets.getString(i - 1))),
                                                        numeroLigneInt,
                                                        "metro"
                                                )
                                        );
                            }
                        }
                    }
                }
            }
        }
    }

    private void addStationsToMap(JSONObject stations) {
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
                allNodes.put(Integer.parseInt(station.get("num").toString()),
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

    public void printGraph() {
        for (Map.Entry<Integer, Node> mapentry : allNodes.entrySet()) {
            System.out.println("*****************************");
            System.out.println("Nom: " + mapentry.getValue().getName());
            System.out.println("ID: " + mapentry.getValue().getId());
            System.out.println("LNG: " + mapentry.getValue().getLng());
            System.out.println("LAT: " + mapentry.getValue().getLat());

            System.out.println("Voisins: ");
            for (int i = 0; i < mapentry.getValue().getEdges().size(); i++) {
                System.out.println("    " + mapentry.getValue().getEdges().get(i).getNodeTo().getName() + ", distance: " + mapentry.getValue().getEdges().get(i).getWeight());
            }

            System.out.println("Lignes: ");
            for (int i = 0; i < mapentry.getValue().getLines().size(); i++) {
                System.out.println("    " + mapentry.getValue().getLines().get(i));
            }
        }
    }
}
