import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Pathfinder {
    private Map<Integer, Node> allNodes = new HashMap<>();

    public Pathfinder(Graph graph) {
        allNodes = graph.getNodes();
    }

    /**
     * Select the current Node for Dijkstra algorithm
     * @return Id of the node ? // TODO
     */
    public int selectNodeForDijkstra() {
        List<Node> sortedNodes = new ArrayList<>();
        if (areAllMarked()) {
            return -1;
        }

        for (int currentNode : allNodes.keySet()) {
            if (!allNodes.get(currentNode).isMarked()) {
                if (sortedNodes.size() == 0) {
                    sortedNodes.add(allNodes.get(currentNode));
                } else {
                    if (allNodes.get(currentNode).getDistanceFromSource() <= sortedNodes.get(0).getDistanceFromSource()) {
                        sortedNodes.add(0, allNodes.get(currentNode));
                    }
                    if (allNodes.get(currentNode).getDistanceFromSource() > sortedNodes.get(0).getDistanceFromSource()) {
                        sortedNodes.add(sortedNodes.size() - 1, allNodes.get(currentNode));
                    }
                }
            }
        }
        return (sortedNodes.get(0).getId());
    }

    public boolean areAllMarked() {
        boolean b = true;
        for (int currentID : allNodes.keySet()) {
            if (!allNodes.get(currentID).isMarked()) {
                b = false;
            }
        }
        return b;
    }

    // Dijkstra algorithm for shortestPath
    public void dijkstra(String sourceString, String targetString) {
        int source = nameToId(sourceString);
        int target = nameToId(targetString);

        System.out.format("Point de départ : %s (id : %s)", sourceString, source);
        System.out.println(); // TODO system line separator
        System.out.format("Arrivée : %s (id : %s)", targetString, target);
        System.out.println();

        int distance = 0;
        int currentNode = source;
        allNodes.get(source).setDistanceFromSource(0);

        while (currentNode != -1) {
            for (Node neighbor : allNodes.get(currentNode).getNeighbors()) {
                if (allNodes.get(currentNode).getDistanceFromSource() + allNodes.get(currentNode).getWeightForNeigbhor(neighbor) < neighbor.getDistanceFromSource()) {
                    neighbor.setDistanceFromSource(allNodes.get(currentNode).getDistanceFromSource() + allNodes.get(currentNode).getWeightForNeigbhor(neighbor));
                    neighbor.setPredecessor(currentNode);
                }
            }
            allNodes.get(currentNode).setMarked(true);

            // Find the new current node
            currentNode = selectNodeForDijkstra();
        }
        distance = allNodes.get(target).getDistanceFromSource();


        List<Integer> cheminLignes = new ArrayList<>();
        List<String> cheminArrets = new ArrayList<>();

        if (distance == Integer.MAX_VALUE) {
            System.out.println("No path found!");
        } else {
            System.out.println("La distance la plus courte est: " + distance);
            int pred = allNodes.get(target).getPredecessor();
            System.out.println("Chemin : ");
            System.out.println(allNodes.get(target).getName());
            cheminArrets.add(allNodes.get(target).getName());
            System.out.println(allNodes.get(target).getLineForNeigbhor(idToNode(allNodes.get(target).getPredecessor())));
            cheminLignes.add(allNodes.get(target).getLineForNeigbhor(idToNode(allNodes.get(target).getPredecessor())));
            while (pred != source) {
                System.out.println(allNodes.get(pred).getName());
                cheminArrets.add(allNodes.get(pred).getName());
                System.out.println(allNodes.get(pred).getLineForNeigbhor(idToNode(allNodes.get(pred).getPredecessor())));
                cheminLignes.add(allNodes.get(pred).getLineForNeigbhor(idToNode(allNodes.get(pred).getPredecessor())));
                pred = allNodes.get(pred).getPredecessor();
            }
            System.out.println(allNodes.get(source).getName());
            cheminArrets.add(allNodes.get(source).getName());
            cheminLignes.add(allNodes.get(pred).getLineForNeigbhor(idToNode(allNodes.get(pred).getPredecessor())));
        }
    }

    public int nameToId(String name) {
        int id;
        for (Map.Entry<Integer, Node> mapentry : allNodes.entrySet()) {
            if (mapentry.getValue().getName().equals(name)) {
                return mapentry.getValue().getId();
            }
        }
        return 0;
    }

    public Node idToNode(int id) {
        for (Map.Entry<Integer, Node> mapentry : allNodes.entrySet()) {
            if (mapentry.getValue().getId() == id) {
                return mapentry.getValue();
            }
        }
        return allNodes.get(0);
    }
}
