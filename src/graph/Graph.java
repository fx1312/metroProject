package graph;

import java.util.*;

public class Graph {
    private Map<Integer, Node> nodes = new HashMap<>();

    public Map<Integer, Node> getNodes() {
        return nodes;
    }

    public void printGraph() {
        for (Map.Entry<Integer, Node> mapentry : nodes.entrySet()) {
            System.out.println("*****************************");
            System.out.println("Nom: " + mapentry.getValue().getName());
            System.out.println("ID: " + mapentry.getKey());
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

    public Node findNodeByName(String name) {
        for (Map.Entry<Integer, Node> mapentry : nodes.entrySet()) {
            if (mapentry.getValue().getName().equals(name)) {
                return mapentry.getValue();
            }
        }
        return null;
    }
}
